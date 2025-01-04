package com.catalis.core.banking.ledger.core.services.directdebit.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.common.core.queries.PaginationUtils;
import com.catalis.core.banking.ledger.core.mappers.directdebit.v1.TransactionLineDirectDebitMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.directdebit.v1.TransactionDirectDebitFilterDTO;
import com.catalis.core.banking.ledger.interfaces.dtos.directdebit.v1.TransactionLineDirectDebitDTO;
import com.catalis.core.banking.ledger.interfaces.enums.directdebit.v1.DirectDebitProcessingStatusEnum;
import com.catalis.core.banking.ledger.interfaces.enums.directdebit.v1.DirectDebitSequenceTypeEnum;
import com.catalis.core.banking.ledger.models.repositories.directdebit.v1.TransactionLineDirectDebitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
public class TransactionLineDirectDebitGetService {

    @Autowired
    private TransactionLineDirectDebitRepository repository;

    @Autowired
    private TransactionLineDirectDebitMapper mapper;

    /**
     * Retrieves a direct debit transaction line by its identifier.
     *
     * @param id the unique identifier of the direct debit transaction line
     * @return a Mono emitting the direct debit transaction line DTO if found, or an empty Mono if not found
     */
    public Mono<TransactionLineDirectDebitDTO> getDirectDebit(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO);
    }

    /**
     * Retrieves a TransactionLineDirectDebitDTO by the given transaction ID.
     *
     * @param transactionId the unique identifier of the transaction to be retrieved
     * @return a Mono emitting the TransactionLineDirectDebitDTO associated with the given transaction ID,
     *         or an empty Mono if no match is found
     */
    public Mono<TransactionLineDirectDebitDTO> getByTransactionId(Long transactionId) {
        return repository.findByTransactionId(transactionId)
                .map(mapper::toDTO);
    }

    /**
     * Searches for direct debit transaction lines based on the provided filter criteria and pagination details.
     *
     * @param filter the filter criteria for searching direct debit transaction lines
     * @param paginationRequest the pagination details including page number and size
     * @return a reactive wrapper containing a paginated response of direct debit transaction line DTOs
     */
    public Mono<PaginationResponse<TransactionLineDirectDebitDTO>> searchDirectDebits(
            TransactionDirectDebitFilterDTO filter,
            PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByCustomCriteria(
                        filter.getMinAmount(),
                        filter.getMaxAmount(),
                        filter.getMandateId(),
                        filter.getCreditorId(),
                        filter.getDebtorName(),
                        filter.getProcessingStatus(),
                        filter.getSequenceType(),
                        filter.getDueDateStart(),
                        filter.getDueDateEnd(),
                        filter.getIsRevoked(),
                        pageable
                ),
                () -> repository.countByCustomCriteria(
                        filter.getMinAmount(),
                        filter.getMaxAmount(),
                        filter.getMandateId(),
                        filter.getCreditorId(),
                        filter.getDebtorName(),
                        filter.getProcessingStatus(),
                        filter.getSequenceType(),
                        filter.getDueDateStart(),
                        filter.getDueDateEnd(),
                        filter.getIsRevoked()
                )
        );
    }

    /**
     * Retrieves a paginated list of direct debit transactions associated with a specific mandate.
     *
     * @param mandateId The unique identifier of the mandate for which direct debit transactions are to be retrieved.
     * @param paginationRequest The pagination request details including page number and page size.
     * @return A Mono containing a PaginationResponse of TransactionLineDirectDebitDTO objects.
     */
    public Mono<PaginationResponse<TransactionLineDirectDebitDTO>> getDirectDebitsByMandate(
            String mandateId,
            PaginationRequest paginationRequest) {
        TransactionDirectDebitFilterDTO filter = TransactionDirectDebitFilterDTO.builder()
                .mandateId(mandateId)
                .build();
        return searchDirectDebits(filter, paginationRequest);
    }

    /**
     * Retrieves a paginated list of direct debit transactions associated with a given creditor.
     *
     * @param creditorId the unique identifier of the creditor for which direct debits are to be retrieved
     * @param paginationRequest an object containing pagination information such as page number and page size
     * @return a Mono containing a paginated response of direct debit transaction details
     */
    public Mono<PaginationResponse<TransactionLineDirectDebitDTO>> getDirectDebitsByCreditor(
            String creditorId,
            PaginationRequest paginationRequest) {
        TransactionDirectDebitFilterDTO filter = TransactionDirectDebitFilterDTO.builder()
                .creditorId(creditorId)
                .build();
        return searchDirectDebits(filter, paginationRequest);
    }

    /**
     * Retrieves a paginated list of pending direct debit transactions for a specific due date.
     *
     * This method fetches direct debits with the processing status of "PENDING"
     * and filters them by the given due date. The result is paginated based on
     * the provided pagination request.
     *
     * @param dueDate the due date to filter the pending direct debits
     * @param paginationRequest the pagination request defining page number, size, and sorting criteria
     * @return a Mono containing a paginated response of TransactionLineDirectDebitDTO objects
     */
    public Mono<PaginationResponse<TransactionLineDirectDebitDTO>> getPendingDirectDebits(
            LocalDate dueDate,
            PaginationRequest paginationRequest) {
        TransactionDirectDebitFilterDTO filter = TransactionDirectDebitFilterDTO.builder()
                .processingStatus(DirectDebitProcessingStatusEnum.PENDING)
                .dueDateStart(dueDate)
                .dueDateEnd(dueDate)
                .isRevoked(false)
                .build();
        return searchDirectDebits(filter, paginationRequest);
    }

    /**
     * Retrieves a paginated list of direct debit transactions filtered by a specified amount range.
     *
     * @param minAmount The minimum amount of the direct debit transactions to filter.
     * @param maxAmount The maximum amount of the direct debit transactions to filter.
     * @param paginationRequest The pagination request object containing details like page number and size.
     * @return A paginated response containing a list of direct debit transactions matching the specified amount range and filter criteria.
     */
    public Mono<PaginationResponse<TransactionLineDirectDebitDTO>> getDirectDebitsByAmountRange(
            BigDecimal minAmount,
            BigDecimal maxAmount,
            PaginationRequest paginationRequest) {
        TransactionDirectDebitFilterDTO filter = TransactionDirectDebitFilterDTO.builder()
                .minAmount(minAmount)
                .maxAmount(maxAmount)
                .isRevoked(false)
                .build();
        return searchDirectDebits(filter, paginationRequest);
    }

    /**
     * Retrieves a paginated list of active recurring direct debit transactions.
     *
     * @param paginationRequest the pagination details including page number, size, and sorting criteria.
     * @return a Mono emitting a PaginationResponse containing a list of TransactionLineDirectDebitDTO objects
     *         representing active recurring direct debit transactions.
     */
    public Mono<PaginationResponse<TransactionLineDirectDebitDTO>> getActiveRecurringDirectDebits(
            PaginationRequest paginationRequest) {
        TransactionDirectDebitFilterDTO filter = TransactionDirectDebitFilterDTO.builder()
                .sequenceType(DirectDebitSequenceTypeEnum.RCUR)
                .processingStatus(DirectDebitProcessingStatusEnum.INITIATED)
                .isRevoked(false)
                .build();
        return searchDirectDebits(filter, paginationRequest);
    }

    /**
     * Retrieves a paginated list of revoked direct debits based on the provided filter criteria.
     *
     * @param fromDate The starting date to filter the revoked direct debits by their due date.
     * @param paginationRequest The pagination request containing page size and page number details.
     * @return A Mono emitting a PaginationResponse containing a list of revoked direct debit transactions.
     */
    public Mono<PaginationResponse<TransactionLineDirectDebitDTO>> getRevokedDirectDebits(
            LocalDate fromDate,
            PaginationRequest paginationRequest) {
        TransactionDirectDebitFilterDTO filter = TransactionDirectDebitFilterDTO.builder()
                .isRevoked(true)
                .dueDateStart(fromDate)
                .build();
        return searchDirectDebits(filter, paginationRequest);
    }

}
