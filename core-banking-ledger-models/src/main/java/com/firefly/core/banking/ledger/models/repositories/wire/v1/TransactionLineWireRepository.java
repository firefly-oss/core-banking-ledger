package com.firefly.core.banking.ledger.models.repositories.wire.v1;

import com.firefly.core.banking.ledger.interfaces.enums.wire.v1.WireTransferPriorityEnum;
import com.firefly.core.banking.ledger.models.entities.wire.v1.TransactionLineWireTransfer;
import com.firefly.core.banking.ledger.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TransactionLineWireRepository extends BaseRepository<TransactionLineWireTransfer, UUID> {
    Mono<TransactionLineWireTransfer> findByTransactionId(UUID transactionId);

    Mono<TransactionLineWireTransfer> findByWireTransferReference(String reference);

    @Query("SELECT * FROM transaction_line_wire_transfer " +
            "WHERE wire_cancelled_flag = :cancelled " +
            "ORDER BY :#{#pageable.sort}")
    Flux<TransactionLineWireTransfer> findByWireCancelledFlag(Boolean cancelled, Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_wire_transfer WHERE wire_cancelled_flag = :cancelled")
    Mono<Long> countByWireCancelledFlag(Boolean cancelled);

    @Query("SELECT * FROM transaction_line_wire_transfer " +
            "WHERE wire_transfer_priority = :priority " +
            "ORDER BY :#{#pageable.sort}")
    Flux<TransactionLineWireTransfer> findByWireTransferPriority(WireTransferPriorityEnum priority, Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_wire_transfer WHERE wire_transfer_priority = :priority")
    Mono<Long> countByWireTransferPriority(WireTransferPriorityEnum priority);

    @Query("SELECT * FROM transaction_line_wire_transfer " +
            "WHERE wire_origin_swift_bic = :swiftCode " +
            "ORDER BY :#{#pageable.sort}")
    Flux<TransactionLineWireTransfer> findOutgoingTransfersBySwift(String swiftCode, Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_wire_transfer WHERE wire_origin_swift_bic = :swiftCode")
    Mono<Long> countOutgoingTransfersBySwift(String swiftCode);

    @Query("SELECT * FROM transaction_line_wire_transfer " +
            "WHERE wire_destination_swift_bic = :swiftCode " +
            "ORDER BY :#{#pageable.sort}")
    Flux<TransactionLineWireTransfer> findIncomingTransfersBySwift(String swiftCode, Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_wire_transfer WHERE wire_destination_swift_bic = :swiftCode")
    Mono<Long> countIncomingTransfersBySwift(String swiftCode);

    @Query("SELECT tlw.* FROM transaction_line_wire_transfer tlw " +
            "JOIN transaction t ON t.transaction_id = tlw.transaction_id " +
            "WHERE (:startDate IS NULL OR tlw.wire_processing_date >= :startDate) " +
            "AND (:endDate IS NULL OR tlw.wire_processing_date <= :endDate) " +
            "AND (:priority IS NULL OR tlw.wire_transfer_priority = :priority) " +
            "AND (:cancelled IS NULL OR tlw.wire_cancelled_flag = :cancelled) " +
            "AND (:minAmount IS NULL OR t.total_amount >= :minAmount) " +
            "AND (:maxAmount IS NULL OR t.total_amount <= :maxAmount) " +
            "AND (:beneficiaryName IS NULL OR tlw.wire_beneficiary_name ILIKE concat('%', :beneficiaryName, '%')) " +
            "AND (:originSwiftCode IS NULL OR tlw.wire_origin_swift_bic = :originSwiftCode) " +
            "AND (:destinationSwiftCode IS NULL OR tlw.wire_destination_swift_bic = :destinationSwiftCode) " +
            "AND (:minExchangeRate IS NULL OR tlw.wire_exchange_rate >= :minExchangeRate) " +
            "AND (:maxExchangeRate IS NULL OR tlw.wire_exchange_rate <= :maxExchangeRate) " +
            "AND (:minFeeAmount IS NULL OR tlw.wire_fee_amount >= :minFeeAmount) " +
            "AND (:maxFeeAmount IS NULL OR tlw.wire_fee_amount <= :maxFeeAmount)")
    Flux<TransactionLineWireTransfer> findByCustomCriteria(
            LocalDateTime startDate,
            LocalDateTime endDate,
            WireTransferPriorityEnum priority,
            Boolean cancelled,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            String beneficiaryName,
            String originSwiftCode,
            String destinationSwiftCode,
            BigDecimal minExchangeRate,
            BigDecimal maxExchangeRate,
            BigDecimal minFeeAmount,
            BigDecimal maxFeeAmount,
            Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_wire_transfer tlw " +
            "JOIN transaction t ON t.transaction_id = tlw.transaction_id " +
            "WHERE (:startDate IS NULL OR tlw.wire_processing_date >= :startDate) " +
            "AND (:endDate IS NULL OR tlw.wire_processing_date <= :endDate) " +
            "AND (:priority IS NULL OR tlw.wire_transfer_priority = :priority) " +
            "AND (:cancelled IS NULL OR tlw.wire_cancelled_flag = :cancelled) " +
            "AND (:minAmount IS NULL OR t.total_amount >= :minAmount) " +
            "AND (:maxAmount IS NULL OR t.total_amount <= :maxAmount) " +
            "AND (:beneficiaryName IS NULL OR tlw.wire_beneficiary_name ILIKE concat('%', :beneficiaryName, '%')) " +
            "AND (:originSwiftCode IS NULL OR tlw.wire_origin_swift_bic = :originSwiftCode) " +
            "AND (:destinationSwiftCode IS NULL OR tlw.wire_destination_swift_bic = :destinationSwiftCode) " +
            "AND (:minExchangeRate IS NULL OR tlw.wire_exchange_rate >= :minExchangeRate) " +
            "AND (:maxExchangeRate IS NULL OR tlw.wire_exchange_rate <= :maxExchangeRate) " +
            "AND (:minFeeAmount IS NULL OR tlw.wire_fee_amount >= :minFeeAmount) " +
            "AND (:maxFeeAmount IS NULL OR tlw.wire_fee_amount <= :maxFeeAmount)")
    Mono<Long> countByCustomCriteria(
            LocalDateTime startDate,
            LocalDateTime endDate,
            WireTransferPriorityEnum priority,
            Boolean cancelled,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            String beneficiaryName,
            String originSwiftCode,
            String destinationSwiftCode,
            BigDecimal minExchangeRate,
            BigDecimal maxExchangeRate,
            BigDecimal minFeeAmount,
            BigDecimal maxFeeAmount);


    @Query("SELECT * FROM transaction_line_wire_transfer " +
            "WHERE wire_beneficiary_name ILIKE concat('%', :beneficiaryName, '%') " +
            "ORDER BY :#{#pageable.sort}")
    Flux<TransactionLineWireTransfer> findByBeneficiaryNameContaining(String beneficiaryName, Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_wire_transfer WHERE wire_beneficiary_name ILIKE concat('%', :beneficiaryName, '%')")
    Mono<Long> countByBeneficiaryNameContaining(String beneficiaryName);

    @Query("SELECT * FROM transaction_line_wire_transfer " +
            "WHERE wire_cancelled_flag = false " +
            "AND wire_reception_status = 'PENDING' " +
            "ORDER BY wire_transfer_priority ASC, wire_processing_date ASC")
    Flux<TransactionLineWireTransfer> findPendingTransfers(Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_wire_transfer " +
            "WHERE wire_cancelled_flag = false " +
            "AND wire_reception_status = 'PENDING'")
    Mono<Long> countPendingTransfers();

    @Query("SELECT * FROM transaction_line_wire_transfer " +
            "WHERE wire_exchange_rate IS NOT NULL " +
            "AND (:minRate IS NULL OR wire_exchange_rate >= :minRate) " +
            "AND (:maxRate IS NULL OR wire_exchange_rate <= :maxRate) " +
            "ORDER BY :#{#pageable.sort}")
    Flux<TransactionLineWireTransfer> findByExchangeRateRange(
            Double minRate,
            Double maxRate,
            Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_wire_transfer " +
            "WHERE wire_exchange_rate IS NOT NULL " +
            "AND (:minRate IS NULL OR wire_exchange_rate >= :minRate) " +
            "AND (:maxRate IS NULL OR wire_exchange_rate <= :maxRate)")
    Mono<Long> countByExchangeRateRange(Double minRate, Double maxRate);
}
