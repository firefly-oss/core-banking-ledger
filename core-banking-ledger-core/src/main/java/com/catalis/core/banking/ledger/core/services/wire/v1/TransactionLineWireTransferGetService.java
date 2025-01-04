package com.catalis.core.banking.ledger.core.services.wire.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.common.core.queries.PaginationUtils;
import com.catalis.core.banking.ledger.core.mappers.wire.v1.TransactionLineWireTransferMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.wire.v1.TransactionLineWireTransferDTO;
import com.catalis.core.banking.ledger.interfaces.dtos.wire.v1.TransactionWireFilterDTO;
import com.catalis.core.banking.ledger.interfaces.enums.wire.v1.WireTransferPriorityEnum;
import com.catalis.core.banking.ledger.models.repositories.wire.v1.TransactionLineWireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TransactionLineWireTransferGetService {

    @Autowired
    private TransactionLineWireRepository repository;

    @Autowired
    private TransactionLineWireTransferMapper mapper;

    /**
     * Retrieves a wire transfer information by its unique identifier.
     *
     * @param id the unique identifier of the wire transfer
     * @return a Mono emitting the TransactionLineWireTransferDTO if found, or an empty Mono if not found
     */
    public Mono<TransactionLineWireTransferDTO> getWireTransfer(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO);
    }

    /**
     * Retrieves a TransactionLineWireTransferDTO object based on the provided transaction ID.
     *
     * @param transactionId the unique identifier of the transaction
     * @return a Mono emitting the corresponding TransactionLineWireTransferDTO,
     *         or an empty Mono if no matching transaction is found
     */
    public Mono<TransactionLineWireTransferDTO> getByTransactionId(Long transactionId) {
        return repository.findByTransactionId(transactionId)
                .map(mapper::toDTO);
    }

    /**
     * Searches for wire transfer transactions based on the specified filtering criteria
     * and pagination parameters.
     *
     * @param filter an instance of {@code TransactionWireFilterDTO} containing the
     *               filtering criteria for searching wire transfers. This includes details
     *               such as date range, priority, amount range, beneficiary name, SWIFT codes,
     *               exchange rate range, and fee amount range.
     * @param paginationRequest an instance of {@code PaginationRequest} containing
     *                          the pagination parameters for the search results,
     *                          including page number and page size.
     * @return a {@code Mono} emitting a {@code PaginationResponse} that contains
     *         a list of {@code TransactionLineWireTransferDTO} objects representing
     *         the wire transfer transactions matching the search criteria.
     */
    public Mono<PaginationResponse<TransactionLineWireTransferDTO>> searchWireTransfers(
            TransactionWireFilterDTO filter,
            PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByCustomCriteria(
                        filter.getStartDate(),
                        filter.getEndDate(),
                        filter.getPriority(),
                        filter.getCancelled(),
                        filter.getMinAmount(),
                        filter.getMaxAmount(),
                        filter.getBeneficiaryName(),
                        filter.getOriginSwiftCodes() != null && !filter.getOriginSwiftCodes().isEmpty()
                                ? filter.getOriginSwiftCodes().get(0)
                                : null,
                        filter.getDestinationSwiftCodes() != null && !filter.getDestinationSwiftCodes().isEmpty()
                                ? filter.getDestinationSwiftCodes().get(0)
                                : null,
                        filter.getMinExchangeRate(),
                        filter.getMaxExchangeRate(),
                        filter.getMinFeeAmount(),
                        filter.getMaxFeeAmount(),
                        pageable
                ),
                () -> repository.countByCustomCriteria(
                        filter.getStartDate(),
                        filter.getEndDate(),
                        filter.getPriority(),
                        filter.getCancelled(),
                        filter.getMinAmount(),
                        filter.getMaxAmount(),
                        filter.getBeneficiaryName(),
                        filter.getOriginSwiftCodes() != null && !filter.getOriginSwiftCodes().isEmpty()
                                ? filter.getOriginSwiftCodes().get(0)
                                : null,
                        filter.getDestinationSwiftCodes() != null && !filter.getDestinationSwiftCodes().isEmpty()
                                ? filter.getDestinationSwiftCodes().get(0)
                                : null,
                        filter.getMinExchangeRate(),
                        filter.getMaxExchangeRate(),
                        filter.getMinFeeAmount(),
                        filter.getMaxFeeAmount()
                )
        );
    }

    /**
     * Retrieves wire transfer transactions filtered by the specified beneficiary name.
     *
     * @param beneficiaryName the name of the beneficiary to filter the wire transfer transactions
     * @param paginationRequest the pagination request containing information for pagination, such as page number and size
     * @return a Mono emitting a PaginationResponse containing a list of TransactionLineWireTransferDTO objects that match the specified filter
     */
    public Mono<PaginationResponse<TransactionLineWireTransferDTO>> getTransfersByBeneficiary(
            String beneficiaryName,
            PaginationRequest paginationRequest) {
        TransactionWireFilterDTO filter = TransactionWireFilterDTO.builder()
                .beneficiaryName(beneficiaryName)
                .build();
        return searchWireTransfers(filter, paginationRequest);
    }

    /**
     * Retrieves a paginated list of wire transfer transactions based on the given SWIFT code and origin/destination flag.
     *
     * @param swiftCode the SWIFT code used to filter the wire transfers
     * @param isOrigin a boolean flag indicating if the SWIFT code corresponds
     *                 to the origin (true) or the destination (false) of the transactions
     * @param paginationRequest an object containing pagination details such as page number and size
     * @return a Mono emitting a paginated response containing wire transfer transaction details
     */
    public Mono<PaginationResponse<TransactionLineWireTransferDTO>> getTransfersBySwiftCode(
            String swiftCode,
            boolean isOrigin,
            PaginationRequest paginationRequest) {
        TransactionWireFilterDTO filter = TransactionWireFilterDTO.builder()
                .originSwiftCodes(isOrigin ? List.of(swiftCode) : null)
                .destinationSwiftCodes(!isOrigin ? List.of(swiftCode) : null)
                .build();
        return searchWireTransfers(filter, paginationRequest);
    }

    /**
     * Retrieves a paginated list of pending wire transfer transactions.
     *
     * @param paginationRequest the pagination request containing details such as page size and page number
     * @return a Mono emitting a PaginationResponse containing a list of TransactionLineWireTransferDTO objects representing the pending wire transfer transactions
     */
    public Mono<PaginationResponse<TransactionLineWireTransferDTO>> getPendingTransfers(
            PaginationRequest paginationRequest) {
        TransactionWireFilterDTO filter = TransactionWireFilterDTO.builder()
                .cancelled(false)
                .wireReceptionStatus("PENDING")
                .build();
        return searchWireTransfers(filter, paginationRequest);
    }

    /**
     * Retrieves high-priority wire transfer transaction lines based on the given pagination request.
     *
     * @param paginationRequest the pagination request containing pagination details such as page number and size
     * @return a Mono emitting a paginated response with a list of high-priority wire transfer transaction line DTOs
     */
    public Mono<PaginationResponse<TransactionLineWireTransferDTO>> getHighPriorityTransfers(
            PaginationRequest paginationRequest) {
        TransactionWireFilterDTO filter = TransactionWireFilterDTO.builder()
                .priority(WireTransferPriorityEnum.HIGH)
                .cancelled(false)
                .build();
        return searchWireTransfers(filter, paginationRequest);
    }

    /**
     * Retrieves wire transfer transactions filtered by a specified amount range.
     *
     * @param minAmount the minimum amount for filtering wire transfer transactions
     * @param maxAmount the maximum amount for filtering wire transfer transactions
     * @param paginationRequest the pagination information for handling paginated results
     * @return a reactive Mono emitting a PaginationResponse containing the filtered TransactionLineWireTransferDTO objects
     */
    public Mono<PaginationResponse<TransactionLineWireTransferDTO>> getTransfersByAmountRange(
            BigDecimal minAmount,
            BigDecimal maxAmount,
            PaginationRequest paginationRequest) {
        TransactionWireFilterDTO filter = TransactionWireFilterDTO.builder()
                .minAmount(minAmount)
                .maxAmount(maxAmount)
                .cancelled(false)
                .build();
        return searchWireTransfers(filter, paginationRequest);
    }

    /**
     * Retrieves a paginated list of wire transfers that fall within the specified exchange rate range.
     *
     * @param minRate the minimum exchange rate to filter the wire transfers
     * @param maxRate the maximum exchange rate to filter the wire transfers
     * @param paginationRequest the pagination details for the list of transfers
     * @return a {@code Mono} containing a paginated response of {@code TransactionLineWireTransferDTO} matching the specified exchange rate range
     */
    public Mono<PaginationResponse<TransactionLineWireTransferDTO>> getTransfersByExchangeRateRange(
            BigDecimal minRate,
            BigDecimal maxRate,
            PaginationRequest paginationRequest) {
        TransactionWireFilterDTO filter = TransactionWireFilterDTO.builder()
                .minExchangeRate(minRate)
                .maxExchangeRate(maxRate)
                .build();
        return searchWireTransfers(filter, paginationRequest);
    }

    /**
     * Retrieves a paginated list of wire transfer transactions that fall within the specified fee range.
     *
     * @param minFee The minimum fee amount for the wire transfers to be retrieved.
     * @param maxFee The maximum fee amount for the wire transfers to be retrieved.
     * @param feeCurrency The currency of the fee. If null, transactions with any fee currency will be included.
     * @param paginationRequest The pagination request containing details about the desired page and page size.
     * @return A {@code Mono} containing a {@code PaginationResponse} with the list of wire transfer transactions
     *         that match the specified fee range and pagination criteria.
     */
    public Mono<PaginationResponse<TransactionLineWireTransferDTO>> getTransfersByFeeRange(
            BigDecimal minFee,
            BigDecimal maxFee,
            String feeCurrency,
            PaginationRequest paginationRequest) {
        TransactionWireFilterDTO filter = TransactionWireFilterDTO.builder()
                .minFeeAmount(minFee)
                .maxFeeAmount(maxFee)
                .feeCurrencies(feeCurrency != null ? List.of(feeCurrency) : null)
                .build();
        return searchWireTransfers(filter, paginationRequest);
    }

}