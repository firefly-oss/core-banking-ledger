package com.firefly.core.banking.ledger.models.repositories.directdebit.v1;

import com.firefly.core.banking.ledger.interfaces.enums.directdebit.v1.DirectDebitProcessingStatusEnum;
import com.firefly.core.banking.ledger.interfaces.enums.directdebit.v1.DirectDebitSequenceTypeEnum;
import com.firefly.core.banking.ledger.models.entities.directdebit.v1.TransactionLineDirectDebit;
import com.firefly.core.banking.ledger.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDate;

public interface TransactionLineDirectDebitRepository extends BaseRepository<TransactionLineDirectDebit, UUID> {

    Mono<TransactionLineDirectDebit> findByTransactionId(UUID transactionId);

    @Query("SELECT * FROM transaction_line_direct_debit " +
            "WHERE direct_debit_mandate_id = :mandateId " +
            "ORDER BY :#{#pageable.sort} " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineDirectDebit> findByDirectDebitMandateId(String mandateId, Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_direct_debit " +
            "WHERE direct_debit_mandate_id = :mandateId")
    Mono<Long> countByDirectDebitMandateId(String mandateId);

    @Query("SELECT * FROM transaction_line_direct_debit " +
            "WHERE direct_debit_creditor_id = :creditorId " +
            "ORDER BY :#{#pageable.sort} " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineDirectDebit> findByDirectDebitCreditorId(String creditorId, Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_direct_debit " +
            "WHERE direct_debit_creditor_id = :creditorId")
    Mono<Long> countByDirectDebitCreditorId(String creditorId);

    @Query("SELECT * FROM transaction_line_direct_debit " +
            "WHERE direct_debit_processing_status = :status " +
            "ORDER BY :#{#pageable.sort} " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineDirectDebit> findByDirectDebitProcessingStatus(
            DirectDebitProcessingStatusEnum status,
            Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_direct_debit " +
            "WHERE direct_debit_processing_status = :status")
    Mono<Long> countByDirectDebitProcessingStatus(DirectDebitProcessingStatusEnum status);

    @Query("SELECT * FROM transaction_line_direct_debit " +
            "WHERE direct_debit_sequence_type = :sequenceType " +
            "AND direct_debit_processing_status = :status " +
            "ORDER BY :#{#pageable.sort} " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineDirectDebit> findBySequenceTypeAndStatus(
            DirectDebitSequenceTypeEnum sequenceType,
            DirectDebitProcessingStatusEnum status,
            Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_direct_debit " +
            "WHERE direct_debit_sequence_type = :sequenceType " +
            "AND direct_debit_processing_status = :status")
    Mono<Long> countBySequenceTypeAndStatus(
            DirectDebitSequenceTypeEnum sequenceType,
            DirectDebitProcessingStatusEnum status);

    @Query("SELECT tldd.* FROM transaction_line_direct_debit tldd " +
            "JOIN transaction t ON t.transaction_id = tldd.transaction_id " +
            "WHERE (:minAmount IS NULL OR t.total_amount >= :minAmount) " +
            "AND (:maxAmount IS NULL OR t.total_amount <= :maxAmount) " +
            "AND (:mandateId IS NULL OR tldd.direct_debit_mandate_id = :mandateId) " +
            "AND (:creditorId IS NULL OR tldd.direct_debit_creditor_id = :creditorId) " +
            "AND (:debtorName IS NULL OR tldd.direct_debit_debtor_name ILIKE concat('%', :debtorName, '%')) " +
            "AND (:processingStatus IS NULL OR tldd.direct_debit_processing_status = :processingStatus) " +
            "AND (:sequenceType IS NULL OR tldd.direct_debit_sequence_type = :sequenceType) " +
            "AND (:dueDateStart IS NULL OR tldd.direct_debit_due_date >= :dueDateStart) " +
            "AND (:dueDateEnd IS NULL OR tldd.direct_debit_due_date <= :dueDateEnd) " +
            "AND (:isRevoked IS NULL OR " +
            "     CASE WHEN :isRevoked = true THEN tldd.direct_debit_revocation_date IS NOT NULL " +
            "          ELSE tldd.direct_debit_revocation_date IS NULL END)")
    Flux<TransactionLineDirectDebit> findByCustomCriteria(
            BigDecimal minAmount,
            BigDecimal maxAmount,
            String mandateId,
            String creditorId,
            String debtorName,
            DirectDebitProcessingStatusEnum processingStatus,
            DirectDebitSequenceTypeEnum sequenceType,
            LocalDate dueDateStart,
            LocalDate dueDateEnd,
            Boolean isRevoked,
            Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_direct_debit tldd " +
            "JOIN transaction t ON t.transaction_id = tldd.transaction_id " +
            "WHERE (:minAmount IS NULL OR t.total_amount >= :minAmount) " +
            "AND (:maxAmount IS NULL OR t.total_amount <= :maxAmount) " +
            "AND (:mandateId IS NULL OR tldd.direct_debit_mandate_id = :mandateId) " +
            "AND (:creditorId IS NULL OR tldd.direct_debit_creditor_id = :creditorId) " +
            "AND (:debtorName IS NULL OR tldd.direct_debit_debtor_name ILIKE concat('%', :debtorName, '%')) " +
            "AND (:processingStatus IS NULL OR tldd.direct_debit_processing_status = :processingStatus) " +
            "AND (:sequenceType IS NULL OR tldd.direct_debit_sequence_type = :sequenceType) " +
            "AND (:dueDateStart IS NULL OR tldd.direct_debit_due_date >= :dueDateStart) " +
            "AND (:dueDateEnd IS NULL OR tldd.direct_debit_due_date <= :dueDateEnd) " +
            "AND (:isRevoked IS NULL OR " +
            "     CASE WHEN :isRevoked = true THEN tldd.direct_debit_revocation_date IS NOT NULL " +
            "          ELSE tldd.direct_debit_revocation_date IS NULL END)")
    Mono<Long> countByCustomCriteria(
            BigDecimal minAmount,
            BigDecimal maxAmount,
            String mandateId,
            String creditorId,
            String debtorName,
            DirectDebitProcessingStatusEnum processingStatus,
            DirectDebitSequenceTypeEnum sequenceType,
            LocalDate dueDateStart,
            LocalDate dueDateEnd,
            Boolean isRevoked);

    @Query("SELECT * FROM transaction_line_direct_debit " +
            "WHERE direct_debit_processing_status = 'INITIATED' " +
            "AND direct_debit_due_date <= :date " +
            "AND direct_debit_revocation_date IS NULL " +
            "ORDER BY :#{#pageable.sort} " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineDirectDebit> findPendingDirectDebitsForProcessing(
            LocalDate date,
            Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_direct_debit " +
            "WHERE direct_debit_processing_status = 'INITIATED' " +
            "AND direct_debit_due_date <= :date " +
            "AND direct_debit_revocation_date IS NULL")
    Mono<Long> countPendingDirectDebitsForProcessing(LocalDate date);

    @Query("SELECT * FROM transaction_line_direct_debit " +
            "WHERE direct_debit_debtor_name ILIKE concat('%', :debtorName, '%') " +
            "ORDER BY :#{#pageable.sort} " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineDirectDebit> findByDebtorNameContaining(
            String debtorName,
            Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_direct_debit " +
            "WHERE direct_debit_debtor_name ILIKE concat('%', :debtorName, '%')")
    Mono<Long> countByDebtorNameContaining(String debtorName);

    @Query("SELECT * FROM transaction_line_direct_debit " +
            "WHERE direct_debit_mandate_id = :mandateId " +
            "AND direct_debit_sequence_type = 'RCUR' " +
            "AND direct_debit_processing_status = 'COMPLETED' " +
            "ORDER BY :#{#pageable.sort} " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineDirectDebit> findCompletedRecurringByMandate(
            String mandateId,
            Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_direct_debit " +
            "WHERE direct_debit_mandate_id = :mandateId " +
            "AND direct_debit_sequence_type = 'RCUR' " +
            "AND direct_debit_processing_status = 'COMPLETED'")
    Mono<Long> countCompletedRecurringByMandate(String mandateId);

}
