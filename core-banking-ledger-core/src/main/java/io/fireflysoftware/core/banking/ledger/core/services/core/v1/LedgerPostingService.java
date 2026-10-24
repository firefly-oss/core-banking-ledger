package io.fireflysoftware.core.banking.ledger.core.services.core.v1;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.DecimalNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.fireflysoftware.core.banking.ledger.core.mappers.core.v1.TransactionMapper;
import io.fireflysoftware.core.banking.ledger.core.mappers.leg.v1.TransactionLegMapper;
import io.fireflysoftware.core.banking.ledger.interfaces.dtos.core.v1.LedgerPostingDTO;
import io.fireflysoftware.core.banking.ledger.interfaces.dtos.core.v1.LedgerPostingRequestDTO;
import io.fireflysoftware.core.banking.ledger.interfaces.dtos.core.v1.LedgerPostingLegDTO;
import io.fireflysoftware.core.banking.ledger.interfaces.dtos.core.v1.LedgerPostingHeaderDTO;
import io.fireflysoftware.core.banking.ledger.interfaces.enums.core.v1.TransactionStatusEnum;
import io.fireflysoftware.core.banking.ledger.models.entities.core.v1.Transaction;
import io.fireflysoftware.core.banking.ledger.models.entities.core.v1.TransactionStatusHistory;
import io.fireflysoftware.core.banking.ledger.models.entities.leg.v1.TransactionLeg;
import io.fireflysoftware.core.banking.ledger.models.repositories.core.v1.TransactionRepository;
import io.fireflysoftware.core.banking.ledger.models.repositories.core.v1.TransactionStatusHistoryRepository;
import io.fireflysoftware.core.banking.ledger.models.repositories.leg.v1.TransactionLegRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.HexFormat;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LedgerPostingService {
    private static final ObjectMapper JSON = new ObjectMapper().registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    private final DatabaseClient database;
    private final TransactionRepository transactions;
    private final TransactionLegRepository legs;
    private final TransactionStatusHistoryRepository history;
    private final TransactionMapper transactionMapper;
    private final TransactionLegMapper legMapper;

    @Transactional
    public Mono<LedgerPostingDTO> create(LedgerPostingRequestDTO request) {
        return createAggregate(request, null);
    }

    private Mono<LedgerPostingDTO> createAggregate(LedgerPostingRequestDTO request, UUID reversalOf) {
        return Mono.defer(() -> {
            validate(request);
            String fingerprint = fingerprint(request, reversalOf);
            return requireFundsInput(request).then(database.sql("INSERT INTO ledger_posting_request(idempotency_key,input_fingerprint) VALUES (:key,:fingerprint) ON CONFLICT DO NOTHING")
                    .bind("key", request.getIdempotencyKey()).bind("fingerprint", fingerprint).fetch().rowsUpdated())
                    .flatMap(inserted -> database.sql("SELECT input_fingerprint,transaction_id FROM ledger_posting_request WHERE idempotency_key = :key")
                            .bind("key", request.getIdempotencyKey())
                            .map((row, metadata) -> new Claim(row.get("input_fingerprint", String.class), row.get("transaction_id", UUID.class)))
                            .one().flatMap(claim -> {
                                if (!fingerprint.equals(claim.fingerprint())) {
                                    return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Idempotency key already belongs to another posting input"));
                                }
                                if (inserted == 0) {
                                    if (claim.transactionId() == null) {
                                        return Mono.error(new IllegalStateException("Committed posting claim has no transaction"));
                                    }
                                    return load(claim.transactionId(), true);
                                }
                                return persist(request, reversalOf);
                            }));
        });
    }

    @Transactional
    public Mono<LedgerPostingDTO> confirm(UUID id) {
        return transition(id, TransactionStatusEnum.POSTED);
    }

    @Transactional
    public Mono<LedgerPostingDTO> release(UUID id) {
        return transition(id, TransactionStatusEnum.REVERSED);
    }

    private Mono<LedgerPostingDTO> transition(UUID id, TransactionStatusEnum target) {
        return lockedPosting(id).flatMap(transaction -> {
            if (transaction.getTransactionStatus() == target) {
                return load(id, true);
            }
            if (target == TransactionStatusEnum.REVERSED && transaction.getTransactionStatus() == TransactionStatusEnum.POSTED) {
                return transactions.findManagedPostedReversal(id).flatMap(reversal -> load(id, true))
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Posted money requires an authoritative linked reversal")));
            }
            if (transaction.getTransactionStatus() != TransactionStatusEnum.PENDING) {
                return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Only pending postings can be confirmed or released"));
            }
            transaction.setTransactionStatus(target);
            TransactionStatusHistory event = new TransactionStatusHistory();
            event.setTransactionId(id);
            event.setStatusCode(target);
            event.setStatusStartDatetime(LocalDateTime.now());
            event.setReason(target == TransactionStatusEnum.POSTED ? "Posting confirmed" : "Pending posting released");
            event.setRegulatedReportingFlag(false);
            return transactions.save(transaction).then(history.save(event)).then(load(id, false));
        });
    }

    @Transactional
    public Mono<LedgerPostingDTO> reverse(UUID originalId, String key) {
        return lockedPosting(originalId).flatMap(original -> {
            if (original.getTransactionStatus() != TransactionStatusEnum.POSTED || "REVERSAL".equals(original.getRelationType())) {
                return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "A reversal requires an original posted aggregate"));
            }
            return transactions.findManagedPostedReversal(originalId)
                    .flatMap(existing -> {
                        if (!existing.getExternalReference().equals(key)) {
                            return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "The original posting already has a reversal"));
                        }
                        return load(existing.getTransactionId(), true);
                    }).switchIfEmpty(Mono.defer(() -> legs.findAllByTransactionIdOrderByTransactionLegId(originalId).collectList()
                            .flatMap(persisted -> {
                                LedgerPostingHeaderDTO header = new LedgerPostingHeaderDTO();
                                header.setAccountId(original.getAccountId());
                                header.setAccountSpaceId(original.getAccountSpaceId());
                                header.setTransactionType(original.getTransactionType());
                                header.setTransactionStatus(LedgerPostingHeaderDTO.InitialStatus.POSTED);
                                header.setTotalAmount(original.getTotalAmount());
                                header.setCurrency(original.getCurrency());
                                LedgerPostingRequestDTO request = new LedgerPostingRequestDTO();
                                request.setIdempotencyKey(key);
                                request.setTransaction(header);
                                request.setLegs(persisted.stream().map(leg -> {
                                    LedgerPostingLegDTO opposite = new LedgerPostingLegDTO();
                                    opposite.setAccountId(leg.getAccountId());
                                    opposite.setAccountSpaceId(leg.getAccountSpaceId());
                                    opposite.setLegType("DEBIT".equals(leg.getLegType()) ? "CREDIT" : "DEBIT");
                                    opposite.setAmount(leg.getAmount());
                                    opposite.setCurrency(leg.getCurrency());
                                    return opposite;
                                }).toList());
                                return createAggregate(request, originalId);
                            })));
        });
    }

    private Mono<Void> requireFundsInput(LedgerPostingRequestDTO request) {
        var accounts=new java.util.ArrayList<UUID>();accounts.add(request.getTransaction().getAccountId());
        request.getLegs().forEach(leg->accounts.add(leg.getAccountId()));
        return reactor.core.publisher.Flux.fromIterable(accounts.stream().distinct().sorted(java.util.Comparator.comparing(UUID::toString)).toList())
            .concatMap(account->database.sql("SELECT pg_advisory_xact_lock(hashtextextended(:account,55601))")
                .bind("account",account.toString()).fetch().rowsUpdated()).then(database.sql("SELECT bool_and(public.funds_reference_allowed('ACCOUNT',a,TRUE)) AND (:managedKey=FALSE OR current_setting('firefly.funds.purpose',true) IN ('FUNDING','RESERVE','REVERSE')) AS allowed FROM unnest(CAST(:accounts AS uuid[])) a")
            .bind("accounts",accounts.toArray(UUID[]::new)).bind("managedKey",request.getIdempotencyKey().startsWith("fnd:"))
            .map((r,m)->Boolean.TRUE.equals(r.get("allowed",Boolean.class))).one().filter(Boolean::booleanValue)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN,"MANAGED_FUNDS_COMMAND_REQUIRED"))).then());
    }

    private Mono<Transaction> lockedPosting(UUID id) {
        return database.sql("SELECT public.funds_reference_allowed('POSTING',:id,TRUE) AS allowed")
                .bind("id",id).map((r,m)->Boolean.TRUE.equals(r.get("allowed",Boolean.class))).one()
                .filter(Boolean::booleanValue).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN,"MANAGED_FUNDS_COMMAND_REQUIRED")))
                .then(transactions.findByIdForUpdate(id))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Ledger posting was not found")))
                .flatMap(transaction -> transaction.isManagedPosting() ? Mono.just(transaction)
                        : Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "The transaction is not a managed posting")));
    }

    private Mono<LedgerPostingDTO> persist(LedgerPostingRequestDTO request, UUID reversalOf) {
        LedgerPostingHeaderDTO header = request.getTransaction();
        Transaction transaction = new Transaction();
        transaction.setAccountId(header.getAccountId());
        transaction.setAccountSpaceId(header.getAccountSpaceId());
        transaction.setTransactionType(header.getTransactionType());
        transaction.setTransactionStatus(TransactionStatusEnum.valueOf(header.getTransactionStatus().name()));
        transaction.setTotalAmount(header.getTotalAmount());
        transaction.setCurrency(header.getCurrency());
        transaction.setDescription(header.getDescription());
        transaction.setRequestId(header.getRequestId());
        transaction.setTransactionDate(header.getTransactionDate());
        transaction.setValueDate(header.getValueDate());
        transaction.setBookingDate(header.getBookingDate());
        transaction.setRelatedTransactionId(reversalOf);
        if (reversalOf != null) transaction.setRelationType("REVERSAL");
        transaction.setManagedPosting(true);
        transaction.setExternalReference(request.getIdempotencyKey());
        LocalDateTime now = LocalDateTime.now();
        if (transaction.getTransactionDate() == null) transaction.setTransactionDate(now);
        if (transaction.getValueDate() == null) transaction.setValueDate(transaction.getTransactionDate());
        if (transaction.getBookingDate() == null) transaction.setBookingDate(transaction.getTransactionDate());
        return transactions.save(transaction).flatMap(saved -> {
            TransactionStatusHistory initial = new TransactionStatusHistory();
            initial.setTransactionId(saved.getTransactionId());
            initial.setStatusCode(saved.getTransactionStatus());
            initial.setStatusStartDatetime(LocalDateTime.now());
            initial.setReason("Atomic ledger posting created");
            initial.setRegulatedReportingFlag(false);
            return history.save(initial).thenMany(Flux.fromIterable(request.getLegs()).concatMap(input -> {
                TransactionLeg leg = new TransactionLeg();
                leg.setTransactionId(saved.getTransactionId());
                leg.setAccountId(input.getAccountId());
                leg.setAccountSpaceId(input.getAccountSpaceId());
                leg.setLegType(input.getLegType());
                leg.setAmount(input.getAmount());
                leg.setCurrency(input.getCurrency());
                leg.setDescription(input.getDescription());
                leg.setValueDate(saved.getValueDate());
                leg.setBookingDate(saved.getBookingDate());
                return legs.save(leg);
            })).then(database.sql("UPDATE ledger_posting_request SET transaction_id = :transaction WHERE idempotency_key = :key")
                    .bind("transaction", saved.getTransactionId()).bind("key", request.getIdempotencyKey()).fetch().rowsUpdated())
                    .then(load(saved.getTransactionId(), false));
        });
    }

    private Mono<LedgerPostingDTO> load(UUID id, boolean replayed) {
        return transactions.findById(id).switchIfEmpty(Mono.error(new IllegalStateException("Posting transaction is missing")))
                .flatMap(transaction -> legs.findAllByTransactionIdOrderByTransactionLegId(id).map(legMapper::toDTO).collectList()
                        .map(details -> new LedgerPostingDTO(transactionMapper.toDTO(transaction), details, replayed)));
    }

    private static void validate(LedgerPostingRequestDTO request) {
        if (request == null || request.getIdempotencyKey() == null || request.getIdempotencyKey().isBlank()
                || request.getIdempotencyKey().length() > 100 || request.getTransaction() == null
                || request.getLegs() == null || request.getLegs().size() < 2 || request.getLegs().size() > 1000) {
            throw invalid("A posting requires an idempotency key, transaction and two to 1000 legs");
        }
        var header = request.getTransaction();
        if (header.getAccountId() == null || header.getTransactionType() == null || header.getCurrency() == null
                || header.getTransactionStatus() == null
                || (header.getExternalReference() != null && !request.getIdempotencyKey().equals(header.getExternalReference()))) {
            throw invalid("Posting header identity, currency and initial status are required");
        }
        try {
            Currency.getInstance(header.getCurrency());
        } catch (IllegalArgumentException error) {
            throw invalid("Posting currency must be an ISO currency code");
        }
        BigDecimal debit = BigDecimal.ZERO;
        BigDecimal credit = BigDecimal.ZERO;
        for (var leg : request.getLegs()) {
            if (leg == null || leg.getAccountId() == null || !header.getCurrency().equals(leg.getCurrency())
                    || !("DEBIT".equals(leg.getLegType()) || "CREDIT".equals(leg.getLegType()))) {
                throw invalid("Posting legs require account identity, currency and debit or credit side");
            }
            checkAmount(leg.getAmount());
            if ("DEBIT".equals(leg.getLegType())) {
                debit = debit.add(leg.getAmount());
            } else {
                credit = credit.add(leg.getAmount());
            }
        }
        checkAmount(header.getTotalAmount());
        if (debit.compareTo(credit) != 0 || debit.compareTo(header.getTotalAmount()) != 0) {
            throw invalid("Posting debit, credit and header totals must balance");
        }
        checkAmount(debit);
    }

    static void checkAmount(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0 || (long) amount.precision() - amount.scale() > 20
                || amount.stripTrailingZeros().scale() > 18) {
            throw invalid("Posting amounts must be positive and fit NUMERIC(38,18) without rounding");
        }
    }

    private static ResponseStatusException invalid(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }

    private static String fingerprint(LedgerPostingRequestDTO request, UUID reversalOf) {
        try {
            ObjectNode input = JSON.valueToTree(request);
            input.put("reversalOf", reversalOf == null ? null : reversalOf.toString());
            JsonNode canonical = normalize(input);
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(canonical.toString().getBytes(StandardCharsets.UTF_8)));
        } catch (java.security.NoSuchAlgorithmException error) {
            throw new IllegalStateException("SHA-256 is unavailable", error);
        }
    }

    private static JsonNode normalize(JsonNode node) {
        if (node.isNumber()) {
            return DecimalNode.valueOf(node.decimalValue().stripTrailingZeros());
        }
        if (node.isObject()) {
            ObjectNode result = JSON.createObjectNode();
            var names = new java.util.ArrayList<String>();
            node.fieldNames().forEachRemaining(names::add);
            names.stream().sorted().forEach(name -> result.set(name, normalize(node.get(name))));
            return result;
        }
        if (node.isArray()) {
            ArrayNode result = JSON.createArrayNode();
            node.forEach(value -> result.add(normalize(value)));
            return result;
        }
        return node;
    }

    private record Claim(String fingerprint, UUID transactionId) {}
}
