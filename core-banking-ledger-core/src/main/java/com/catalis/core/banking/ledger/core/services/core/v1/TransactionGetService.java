package com.catalis.core.banking.ledger.core.services.core.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.common.core.queries.PaginationUtils;
import com.catalis.core.banking.ledger.core.mappers.core.v1.TransactionMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionDTO;
import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionFilterDTO;
import com.catalis.core.banking.ledger.models.repositories.core.v1.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional(readOnly = true)
public class TransactionGetService {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private TransactionMapper mapper;

    /**
     * Retrieves a transaction by its unique identifier.
     *
     * @param id the unique identifier of the transaction to retrieve
     * @return a {@code Mono} emitting the {@code TransactionDTO} if found, or an error if the transaction
     *         does not exist or an issue occurs during retrieval
     */
    public Mono<TransactionDTO> getTransaction(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("Transaction not found with id: " + id)))
                .onErrorResume(e -> Mono.error(new RuntimeException("Error retrieving transaction with id: " + id, e)));
    }

    /**
     * Retrieves a paginated list of transactions associated with a specific account ID.
     *
     * @param accountId the unique identifier of the account whose transactions are to be retrieved
     * @param paginationRequest an object containing pagination details such as page number and page size
     * @return a Mono emitting a paginated response containing a list of TransactionDTO objects
     */
    public Mono<PaginationResponse<TransactionDTO>> getTransactionsByAccount(Long accountId, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAccountId(accountId, pageable),
                () -> repository.countByAccountId(accountId)
        );
    }

    /**
     * Retrieves a paginated list of all transactions.
     *
     * @param paginationRequest the object containing pagination parameters such as page number and size
     * @return a `Mono` that emits a `PaginationResponse` containing a list of `TransactionDTO` objects and pagination metadata
     */
    public Mono<PaginationResponse<TransactionDTO>> getAllTransactions(PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findAllBy(pageable)
                        .onErrorResume(e -> Mono.error(new RuntimeException("Error retrieving all transactions", e))),
                () -> repository.count()
                        .onErrorResume(e -> Mono.error(new RuntimeException("Error counting all transactions", e)))
        ).onErrorResume(e -> Mono.error(new RuntimeException("Error during pagination for all transactions", e)));
    }

    /**
     * Searches for transactions based on the provided filter and pagination parameters.
     *
     * @param filter the object containing criteria to filter transactions, such as date range,
     *               amount range, currencies, transaction types, and other attributes
     * @param paginationRequest the object specifying pagination parameters, such as page size,
     *                          page number, and sorting preferences
     * @return a {@code Mono} that emits a {@code PaginationResponse} containing a list of
     *         filtered {@code TransactionDTO} objects along with pagination metadata,
     *         or an error if an issue occurs during the query, filtering, or pagination process
     */
    public Mono<PaginationResponse<TransactionDTO>> searchTransactions(
            TransactionFilterDTO filter,
            PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findTransactionsByFilter(
                        filter.getStartDate(),
                        filter.getEndDate(),
                        filter.getMinAmount(),
                        filter.getMaxAmount(),
                        filter.getCurrencies(),
                        filter.getTypes(),
                        filter.getStatuses(),
                        filter.getCategoryIds(),
                        filter.getAccountIds(),
                        filter.getReferenceNumber(),
                        filter.getDescription(),
                        filter.getInitiatingParty(),
                        filter.getIncludeReversed(),
                        filter.getOnlyFailed(),
                        filter.getOnlyPending(),
                        pageable.getPageSize(),
                        Math.toIntExact(pageable.getOffset()),
                        pageable.getSort().isSorted() ? pageable.getSort().toString().split(":")[0].trim() : "transaction_date",
                        pageable.getSort().isSorted() ? pageable.getSort().toString().split(":")[1].trim().toUpperCase() : "ASC"
                ).onErrorResume(e -> Mono.error(new RuntimeException("Error searching transactions", e))),
                () -> repository.countTransactionsByFilter(
                        filter.getStartDate(),
                        filter.getEndDate(),
                        filter.getMinAmount(),
                        filter.getMaxAmount(),
                        filter.getCurrencies(),
                        filter.getTypes(),
                        filter.getStatuses(),
                        filter.getCategoryIds(),
                        filter.getAccountIds(),
                        filter.getReferenceNumber(),
                        filter.getDescription(),
                        filter.getInitiatingParty(),
                        filter.getIncludeReversed(),
                        filter.getOnlyFailed(),
                        filter.getOnlyPending()
                ).onErrorResume(e -> Mono.error(new RuntimeException("Error counting searched transactions", e)))
        ).onErrorResume(e -> Mono.error(new RuntimeException("Error during pagination for searched transactions", e)));
    }
}