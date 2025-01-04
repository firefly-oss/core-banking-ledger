package com.catalis.core.banking.ledger.models.repositories.sepa.v1;

import com.catalis.core.banking.ledger.interfaces.enums.sepa.v1.SepaTransactionStatusEnum;
import com.catalis.core.banking.ledger.models.entities.sepa.v1.TransactionLineSepaTransfer;
import com.catalis.core.banking.ledger.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TransactionLineSepaRepository extends BaseRepository<TransactionLineSepaTransfer, Long> {
    Mono<TransactionLineSepaTransfer> findByTransactionId(Long transactionId);

    Mono<TransactionLineSepaTransfer> findBySepaEndToEndId(String endToEndId);

    Flux<TransactionLineSepaTransfer> findBySepaCreditorId(String creditorId, Pageable pageable);

    Mono<Long> countBySepaCreditorId(String creditorId);

    Flux<TransactionLineSepaTransfer> findBySepaDebtorId(String debtorId, Pageable pageable);

    Mono<Long> countBySepaDebtorId(String debtorId);

    Flux<TransactionLineSepaTransfer> findBySepaTransactionStatus(SepaTransactionStatusEnum status, Pageable pageable);

    Mono<Long> countBySepaTransactionStatus(SepaTransactionStatusEnum status);

    @Query("SELECT * FROM transaction_line_sepa_transfer " +
            "WHERE sepa_origin_iban = :iban " +
            "ORDER BY sepa_processing_date DESC")
    Flux<TransactionLineSepaTransfer> findOutgoingTransfersByIban(String iban, Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_sepa_transfer " +
            "WHERE sepa_origin_iban = :iban")
    Mono<Long> countOutgoingTransfersByIban(String iban);

    @Query("SELECT * FROM transaction_line_sepa_transfer " +
            "WHERE sepa_destination_iban = :iban " +
            "ORDER BY sepa_processing_date DESC")
    Flux<TransactionLineSepaTransfer> findIncomingTransfersByIban(String iban, Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_sepa_transfer " +
            "WHERE sepa_destination_iban = :iban")
    Mono<Long> countIncomingTransfersByIban(String iban);

    @Query("SELECT tls.* FROM transaction_line_sepa_transfer tls " +
            "JOIN transaction t ON t.transaction_id = tls.transaction_id " +
            "WHERE (:startDate IS NULL OR tls.sepa_processing_date >= :startDate) " +
            "AND (:endDate IS NULL OR tls.sepa_processing_date <= :endDate) " +
            "AND (:status IS NULL OR tls.sepa_transaction_status = :status) " +
            "AND (:creditorId IS NULL OR tls.sepa_creditor_id = :creditorId) " +
            "AND (:minAmount IS NULL OR t.total_amount >= :minAmount) " +
            "AND (:maxAmount IS NULL OR t.total_amount <= :maxAmount)")
    Flux<TransactionLineSepaTransfer> findByCustomCriteria(
            LocalDateTime startDate,
            LocalDateTime endDate,
            SepaTransactionStatusEnum status,
            String creditorId,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_sepa_transfer tls " +
            "JOIN transaction t ON t.transaction_id = tls.transaction_id " +
            "WHERE (:startDate IS NULL OR tls.sepa_processing_date >= :startDate) " +
            "AND (:endDate IS NULL OR tls.sepa_processing_date <= :endDate) " +
            "AND (:status IS NULL OR tls.sepa_transaction_status = :status) " +
            "AND (:creditorId IS NULL OR tls.sepa_creditor_id = :creditorId) " +
            "AND (:minAmount IS NULL OR t.total_amount >= :minAmount) " +
            "AND (:maxAmount IS NULL OR t.total_amount <= :maxAmount)")
    Mono<Long> countByCustomCriteria(
            LocalDateTime startDate,
            LocalDateTime endDate,
            SepaTransactionStatusEnum status,
            String creditorId,
            BigDecimal minAmount,
            BigDecimal maxAmount);


    @Query("SELECT * FROM transaction_line_sepa_transfer " +
            "WHERE sepa_transaction_status = 'PDNG' " +
            "AND sepa_requested_execution_date <= :date " +
            "ORDER BY sepa_requested_execution_date ASC")
    Flux<TransactionLineSepaTransfer> findPendingTransfersForExecution(LocalDateTime date, Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_sepa_transfer " +
            "WHERE sepa_transaction_status = 'PDNG' " +
            "AND sepa_requested_execution_date <= :date")
    Mono<Long> countPendingTransfersForExecution(LocalDateTime date);
}