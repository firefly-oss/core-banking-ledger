package com.catalis.core.banking.ledger.core.services.statement.v1;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.common.core.queries.PaginationUtils;
import com.catalis.core.banking.ledger.core.mappers.core.v1.TransactionMapper;
import com.catalis.core.banking.ledger.core.mappers.statement.v1.StatementMapper;
import com.catalis.core.banking.ledger.core.services.core.v1.TransactionService;
import com.catalis.core.banking.ledger.core.services.event.v1.EventOutboxService;
import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionDTO;
import com.catalis.core.banking.ledger.interfaces.dtos.statement.v1.*;
import com.catalis.core.banking.ledger.interfaces.enums.core.v1.TransactionStatusEnum;
import com.catalis.core.banking.ledger.interfaces.enums.statement.v1.StatementPeriodEnum;
import com.catalis.core.banking.ledger.models.entities.statement.v1.Statement;
import com.catalis.core.banking.ledger.models.repositories.statement.v1.StatementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Implementation of the StatementService interface.
 */
@Service
@Transactional
public class StatementServiceImpl implements StatementService {

    @Autowired
    private StatementRepository repository;

    @Autowired
    private StatementMapper mapper;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private EventOutboxService eventOutboxService;

    @Override
    public Mono<StatementDTO> generateAccountStatement(Long accountId, StatementRequestDTO requestDTO) {
        // Determine date range based on request
        Pair<LocalDate, LocalDate> dateRange = calculateDateRange(requestDTO);
        LocalDate startDate = dateRange.getFirst();
        LocalDate endDate = dateRange.getSecond();

        // Create statement metadata
        StatementMetadataDTO metadata = new StatementMetadataDTO();
        metadata.setAccountId(accountId);
        metadata.setPeriodType(requestDTO.getPeriodType());
        metadata.setStartDate(startDate);
        metadata.setEndDate(endDate);
        metadata.setGenerationDate(LocalDateTime.now());
        metadata.setIncludedPending(requestDTO.getIncludePending() != null ? requestDTO.getIncludePending() : false);
        metadata.setIncludedDetails(requestDTO.getIncludeDetails() != null ? requestDTO.getIncludeDetails() : true);

        // Fetch transactions for the account within the date range
        return fetchAccountTransactions(accountId, startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay(), metadata.getIncludedPending())
                .collectList()
                .flatMap(transactions -> {
                    // Create statement entries
                    List<StatementEntryDTO> entries = createStatementEntries(transactions);

                    // Calculate statement totals
                    BigDecimal openingBalance = calculateOpeningBalance(entries);
                    BigDecimal closingBalance = calculateClosingBalance(entries);
                    BigDecimal totalCredits = calculateTotalCredits(entries);
                    BigDecimal totalDebits = calculateTotalDebits(entries);

                    // Create statement DTO
                    StatementDTO statementDTO = new StatementDTO();
                    statementDTO.setMetadata(metadata);
                    statementDTO.setOpeningBalance(openingBalance);
                    statementDTO.setClosingBalance(closingBalance);
                    statementDTO.setTotalCredits(totalCredits);
                    statementDTO.setTotalDebits(totalDebits);
                    statementDTO.setEntries(entries);

                    metadata.setTransactionCount(entries.size());

                    // Save statement metadata to database
                    Statement entity = mapper.toEntity(metadata);
                    return repository.save(entity)
                            .flatMap(savedEntity -> {
                                metadata.setStatementId(savedEntity.getStatementId());
                                statementDTO.setMetadata(metadata);

                                // Publish statement generated event
                                return eventOutboxService.publishEvent(
                                        "STATEMENT",
                                        savedEntity.getStatementId().toString(),
                                        "ACCOUNT_STATEMENT_GENERATED",
                                        metadata
                                ).then(Mono.just(statementDTO));
                            });
                });
    }

    @Override
    public Mono<StatementDTO> generateAccountSpaceStatement(Long accountSpaceId, StatementRequestDTO requestDTO) {
        // Determine date range based on request
        Pair<LocalDate, LocalDate> dateRange = calculateDateRange(requestDTO);
        LocalDate startDate = dateRange.getFirst();
        LocalDate endDate = dateRange.getSecond();

        // Create statement metadata
        StatementMetadataDTO metadata = new StatementMetadataDTO();
        metadata.setAccountSpaceId(accountSpaceId);
        metadata.setPeriodType(requestDTO.getPeriodType());
        metadata.setStartDate(startDate);
        metadata.setEndDate(endDate);
        metadata.setGenerationDate(LocalDateTime.now());
        metadata.setIncludedPending(requestDTO.getIncludePending() != null ? requestDTO.getIncludePending() : false);
        metadata.setIncludedDetails(requestDTO.getIncludeDetails() != null ? requestDTO.getIncludeDetails() : true);

        // Fetch transactions for the account space within the date range
        return fetchAccountSpaceTransactions(accountSpaceId, startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay(), metadata.getIncludedPending())
                .collectList()
                .flatMap(transactions -> {
                    // Create statement entries
                    List<StatementEntryDTO> entries = createStatementEntries(transactions);

                    // Calculate statement totals
                    BigDecimal openingBalance = calculateOpeningBalance(entries);
                    BigDecimal closingBalance = calculateClosingBalance(entries);
                    BigDecimal totalCredits = calculateTotalCredits(entries);
                    BigDecimal totalDebits = calculateTotalDebits(entries);

                    // Create statement DTO
                    StatementDTO statementDTO = new StatementDTO();
                    statementDTO.setMetadata(metadata);
                    statementDTO.setOpeningBalance(openingBalance);
                    statementDTO.setClosingBalance(closingBalance);
                    statementDTO.setTotalCredits(totalCredits);
                    statementDTO.setTotalDebits(totalDebits);
                    statementDTO.setEntries(entries);

                    metadata.setTransactionCount(entries.size());

                    // Save statement metadata to database
                    Statement entity = mapper.toEntity(metadata);
                    return repository.save(entity)
                            .flatMap(savedEntity -> {
                                metadata.setStatementId(savedEntity.getStatementId());
                                statementDTO.setMetadata(metadata);

                                // Publish statement generated event
                                return eventOutboxService.publishEvent(
                                        "STATEMENT",
                                        savedEntity.getStatementId().toString(),
                                        "ACCOUNT_SPACE_STATEMENT_GENERATED",
                                        metadata
                                ).then(Mono.just(statementDTO));
                            });
                });
    }

    @Override
    public Mono<StatementMetadataDTO> getStatement(Long statementId) {
        return repository.findById(statementId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<StatementMetadataDTO>> listAccountStatements(Long accountId, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAccountId(accountId, pageable),
                () -> repository.countByAccountId(accountId)
        );
    }

    @Override
    public Mono<PaginationResponse<StatementMetadataDTO>> listAccountSpaceStatements(Long accountSpaceId, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAccountSpaceId(accountSpaceId, pageable),
                () -> repository.countByAccountSpaceId(accountSpaceId)
        );
    }

    @Override
    public Mono<PaginationResponse<StatementMetadataDTO>> listAccountStatementsByDateRange(
            Long accountId, LocalDate startDate, LocalDate endDate, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAccountIdAndDateRange(accountId, startDate, endDate, pageable),
                () -> repository.countByAccountId(accountId) // This is a simplification, ideally we'd count only within the date range
        );
    }

    @Override
    public Mono<PaginationResponse<StatementMetadataDTO>> listAccountSpaceStatementsByDateRange(
            Long accountSpaceId, LocalDate startDate, LocalDate endDate, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAccountSpaceIdAndDateRange(accountSpaceId, startDate, endDate, pageable),
                () -> repository.countByAccountSpaceId(accountSpaceId) // This is a simplification, ideally we'd count only within the date range
        );
    }

    /**
     * Calculate the date range based on the statement request.
     *
     * @param requestDTO The statement request.
     * @return A pair of start and end dates.
     */
    private Pair<LocalDate, LocalDate> calculateDateRange(StatementRequestDTO requestDTO) {
        LocalDate startDate;
        LocalDate endDate;

        switch (requestDTO.getPeriodType()) {
            case MONTHLY:
                int month = requestDTO.getMonth() != null ? requestDTO.getMonth() : LocalDate.now().getMonthValue();
                int year = requestDTO.getYear() != null ? requestDTO.getYear() : LocalDate.now().getYear();
                startDate = LocalDate.of(year, month, 1);
                endDate = startDate.plusMonths(1).minusDays(1);
                break;

            case QUARTERLY:
                int quarter = requestDTO.getQuarter() != null ? requestDTO.getQuarter() : ((LocalDate.now().getMonthValue() - 1) / 3) + 1;
                year = requestDTO.getYear() != null ? requestDTO.getYear() : LocalDate.now().getYear();
                int startMonth = (quarter - 1) * 3 + 1;
                startDate = LocalDate.of(year, startMonth, 1);
                endDate = startDate.plusMonths(3).minusDays(1);
                break;

            case YEARLY:
                year = requestDTO.getYear() != null ? requestDTO.getYear() : LocalDate.now().getYear();
                startDate = LocalDate.of(year, 1, 1);
                endDate = LocalDate.of(year, 12, 31);
                break;

            case CUSTOM:
                if (requestDTO.getStartDate() == null || requestDTO.getEndDate() == null) {
                    throw new IllegalArgumentException("Start date and end date are required for custom period");
                }
                startDate = requestDTO.getStartDate();
                endDate = requestDTO.getEndDate();
                break;

            default:
                // Default to current month
                startDate = LocalDate.now().withDayOfMonth(1);
                endDate = startDate.plusMonths(1).minusDays(1);
                break;
        }

        return new Pair<>(startDate, endDate);
    }

    /**
     * Fetch transactions for an account within a date range.
     *
     * @param accountId The account ID.
     * @param startDateTime The start date and time.
     * @param endDateTime The end date and time.
     * @param includePending Whether to include pending transactions.
     * @return A flux of transactions.
     */
    private Flux<TransactionDTO> fetchAccountTransactions(Long accountId, LocalDateTime startDateTime, LocalDateTime endDateTime, boolean includePending) {
        // Use the transaction service to get all transactions for the account
        return transactionService.getTransactionsByAccountId(accountId)
                // Filter by date range
                .filter(transaction -> {
                    LocalDateTime txnDate = transaction.getTransactionDate();
                    return txnDate != null &&
                           !txnDate.isBefore(startDateTime) &&
                           !txnDate.isAfter(endDateTime);
                })
                // Filter by status if not including pending transactions
                .filter(transaction -> includePending || !TransactionStatusEnum.PENDING.equals(transaction.getTransactionStatus()));
    }

    /**
     * Fetch transactions for an account space within a date range.
     *
     * @param accountSpaceId The account space ID.
     * @param startDateTime The start date and time.
     * @param endDateTime The end date and time.
     * @param includePending Whether to include pending transactions.
     * @return A flux of transactions.
     */
    private Flux<TransactionDTO> fetchAccountSpaceTransactions(Long accountSpaceId, LocalDateTime startDateTime, LocalDateTime endDateTime, boolean includePending) {
        // Use the transaction service to get all transactions for the account space
        return transactionService.getTransactionsByAccountSpaceId(accountSpaceId)
                // Filter by date range
                .filter(transaction -> {
                    LocalDateTime txnDate = transaction.getTransactionDate();
                    return txnDate != null &&
                           !txnDate.isBefore(startDateTime) &&
                           !txnDate.isAfter(endDateTime);
                })
                // Filter by status if not including pending transactions
                .filter(transaction -> includePending || !TransactionStatusEnum.PENDING.equals(transaction.getTransactionStatus()));
    }

    /**
     * Create statement entries from transactions.
     *
     * @param transactions The list of transactions.
     * @return The list of statement entries.
     */
    private List<StatementEntryDTO> createStatementEntries(List<TransactionDTO> transactions) {
        List<StatementEntryDTO> entries = new ArrayList<>();
        BigDecimal runningBalance = BigDecimal.ZERO; // This would be initialized with the actual opening balance

        // Sort transactions by value date
        transactions.sort(Comparator.comparing(TransactionDTO::getValueDate));

        for (TransactionDTO transaction : transactions) {
            StatementEntryDTO entry = new StatementEntryDTO();
            entry.setTransactionId(transaction.getTransactionId());
            entry.setTransactionDate(transaction.getTransactionDate());
            entry.setValueDate(transaction.getValueDate());
            entry.setBookingDate(transaction.getBookingDate());
            entry.setTransactionType(transaction.getTransactionType());
            entry.setTransactionStatus(transaction.getTransactionStatus());
            entry.setAmount(transaction.getTotalAmount());
            entry.setCurrency(transaction.getCurrency());
            entry.setDescription(transaction.getDescription());
            entry.setInitiatingParty(transaction.getInitiatingParty());
            entry.setExternalReference(transaction.getExternalReference());
            entry.setTransactionCategoryId(transaction.getTransactionCategoryId());

            // Update running balance
            runningBalance = runningBalance.add(transaction.getTotalAmount());
            entry.setRunningBalance(runningBalance);

            entries.add(entry);
        }

        return entries;
    }

    /**
     * Calculate the opening balance from statement entries.
     *
     * @param entries The list of statement entries.
     * @return The opening balance.
     */
    private BigDecimal calculateOpeningBalance(List<StatementEntryDTO> entries) {
        if (entries.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // Get the first entry's running balance and subtract its amount to get the opening balance
        StatementEntryDTO firstEntry = entries.get(0);
        return firstEntry.getRunningBalance().subtract(firstEntry.getAmount());
    }

    /**
     * Calculate the closing balance from statement entries.
     *
     * @param entries The list of statement entries.
     * @return The closing balance.
     */
    private BigDecimal calculateClosingBalance(List<StatementEntryDTO> entries) {
        if (entries.isEmpty()) {
            return BigDecimal.ZERO;
        }
        // The running balance of the last entry is the closing balance
        return entries.get(entries.size() - 1).getRunningBalance();
    }

    /**
     * Calculate the total credits from statement entries.
     *
     * @param entries The list of statement entries.
     * @return The total credits.
     */
    private BigDecimal calculateTotalCredits(List<StatementEntryDTO> entries) {
        return entries.stream()
                .filter(entry -> entry.getAmount().compareTo(BigDecimal.ZERO) > 0)
                .map(StatementEntryDTO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calculate the total debits from statement entries.
     *
     * @param entries The list of statement entries.
     * @return The total debits.
     */
    private BigDecimal calculateTotalDebits(List<StatementEntryDTO> entries) {
        return entries.stream()
                .filter(entry -> entry.getAmount().compareTo(BigDecimal.ZERO) < 0)
                .map(entry -> entry.getAmount().abs()) // Convert to positive for sum
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Simple pair class for returning two values.
     *
     * @param <T> The type of the first value.
     * @param <U> The type of the second value.
     */
    private static class Pair<T, U> {
        private final T first;
        private final U second;

        public Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }

        public T getFirst() {
            return first;
        }

        public U getSecond() {
            return second;
        }
    }
}
