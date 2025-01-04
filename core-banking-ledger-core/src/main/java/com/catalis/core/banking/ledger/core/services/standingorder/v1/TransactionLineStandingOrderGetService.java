package com.catalis.core.banking.ledger.core.services.standingorder.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.common.core.queries.PaginationUtils;
import com.catalis.core.banking.ledger.core.mappers.standingorder.v1.TransactionLineStandingOrderMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.standingorder.v1.TransactionLineStandingOrderDTO;
import com.catalis.core.banking.ledger.interfaces.dtos.standingorder.v1.TransactionStandingOrderFilterDTO;
import com.catalis.core.banking.ledger.interfaces.enums.standingorder.v1.StandingOrderFrequencyEnum;
import com.catalis.core.banking.ledger.interfaces.enums.standingorder.v1.StandingOrderStatusEnum;
import com.catalis.core.banking.ledger.models.repositories.standingorder.v1.TransactionLineStandingOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TransactionLineStandingOrderGetService {

    @Autowired
    private TransactionLineStandingOrderRepository repository;

    @Autowired
    private TransactionLineStandingOrderMapper mapper;

    /**
     * Retrieves a standing order by its unique identifier.
     *
     * @param id the unique identifier of the standing order to retrieve
     * @return a Mono emitting the corresponding TransactionLineStandingOrderDTO if found, or an empty Mono if no standing order is found for the given id
     */
    public Mono<TransactionLineStandingOrderDTO> getStandingOrder(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO);
    }

    /**
     * Retrieves a {@code TransactionLineStandingOrderDTO} based on the given transaction ID.
     *
     * @param transactionId The unique identifier of the transaction to retrieve details for.
     * @return A {@code Mono} emitting the {@code TransactionLineStandingOrderDTO} if found, or an empty Mono if no transaction matches the ID.
     */
    public Mono<TransactionLineStandingOrderDTO> getByTransactionId(Long transactionId) {
        return repository.findByTransactionId(transactionId)
                .map(mapper::toDTO);
    }

    /**
     * Searches for standing orders based on the provided filter and pagination request.
     *
     * @param filter the filter criteria for searching standing orders, including parameters such as amount range,
     *               recipient details, status, frequency, date range, and other attributes.
     * @param paginationRequest the pagination details, including page number and size, used to fetch the results.
     * @return a Mono containing a paginated response of standing order DTOs matching the specified criteria.
     */
    public Mono<PaginationResponse<TransactionLineStandingOrderDTO>> searchStandingOrders(
            TransactionStandingOrderFilterDTO filter,
            PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByCustomCriteria(
                        filter.getMinAmount(),
                        filter.getMaxAmount(),
                        filter.getStandingOrderId(),
                        filter.getRecipientName(),
                        filter.getRecipientIbans() != null && !filter.getRecipientIbans().isEmpty()
                                ? filter.getRecipientIbans().get(0)
                                : null,
                        filter.getRecipientBics() != null && !filter.getRecipientBics().isEmpty()
                                ? filter.getRecipientBics().get(0)
                                : null,
                        filter.getStatus(),
                        filter.getFrequency(),
                        filter.getStartDateFrom(),
                        filter.getStartDateTo(),
                        filter.getEndDateFrom(),
                        filter.getEndDateTo(),
                        filter.getNextExecutionDateFrom(),
                        filter.getNextExecutionDateTo(),
                        filter.getIsSuspended(),
                        filter.getMinTotalExecutions(),
                        filter.getMaxTotalExecutions(),
                        filter.getCreatedBy(),
                        filter.getUpdatedBy(),
                        pageable
                ),
                () -> repository.countByCustomCriteria(
                        filter.getMinAmount(),
                        filter.getMaxAmount(),
                        filter.getStandingOrderId(),
                        filter.getRecipientName(),
                        filter.getRecipientIbans() != null && !filter.getRecipientIbans().isEmpty()
                                ? filter.getRecipientIbans().get(0)
                                : null,
                        filter.getRecipientBics() != null && !filter.getRecipientBics().isEmpty()
                                ? filter.getRecipientBics().get(0)
                                : null,
                        filter.getStatus(),
                        filter.getFrequency(),
                        filter.getStartDateFrom(),
                        filter.getStartDateTo(),
                        filter.getEndDateFrom(),
                        filter.getEndDateTo(),
                        filter.getNextExecutionDateFrom(),
                        filter.getNextExecutionDateTo(),
                        filter.getIsSuspended(),
                        filter.getMinTotalExecutions(),
                        filter.getMaxTotalExecutions(),
                        filter.getCreatedBy(),
                        filter.getUpdatedBy()
                )
        );
    }

    /**
     * Retrieves a paginated list of active standing orders based on the provided pagination request.
     *
     * @param paginationRequest the request object containing pagination details such as page number and size
     * @return a reactive {@code Mono} emitting a {@code PaginationResponse} which contains the list of active standing orders
     */
    public Mono<PaginationResponse<TransactionLineStandingOrderDTO>> getActiveStandingOrders(
            PaginationRequest paginationRequest) {
        TransactionStandingOrderFilterDTO filter = TransactionStandingOrderFilterDTO.builder()
                .status(StandingOrderStatusEnum.ACTIVE)
                .isSuspended(false)
                .build();
        return searchStandingOrders(filter, paginationRequest);
    }

    /**
     * Retrieves a paginated list of standing orders filtered by a specified amount range.
     *
     * @param minAmount the minimum amount for filtering standing orders
     * @param maxAmount the maximum amount for filtering standing orders
     * @param paginationRequest the pagination parameters including page number and size
     * @return a Mono containing a paginated response of TransactionLineStandingOrderDTO objects
     */
    public Mono<PaginationResponse<TransactionLineStandingOrderDTO>> getStandingOrdersByAmountRange(
            BigDecimal minAmount,
            BigDecimal maxAmount,
            PaginationRequest paginationRequest) {
        TransactionStandingOrderFilterDTO filter = TransactionStandingOrderFilterDTO.builder()
                .minAmount(minAmount)
                .maxAmount(maxAmount)
                .status(StandingOrderStatusEnum.ACTIVE)
                .build();
        return searchStandingOrders(filter, paginationRequest);
    }

    /**
     * Retrieves standing orders filtered by a specific frequency.
     *
     * @param frequency         the frequency of the standing orders to filter by, represented as a StandingOrderFrequencyEnum
     * @param paginationRequest the pagination request containing page number, size, and sorting information
     * @return a Mono containing a PaginationResponse with a list of TransactionLineStandingOrderDTO matching the specified frequency
     */
    public Mono<PaginationResponse<TransactionLineStandingOrderDTO>> getStandingOrdersByFrequency(
            StandingOrderFrequencyEnum frequency,
            PaginationRequest paginationRequest) {
        TransactionStandingOrderFilterDTO filter = TransactionStandingOrderFilterDTO.builder()
                .frequency(frequency)
                .status(StandingOrderStatusEnum.ACTIVE)
                .build();
        return searchStandingOrders(filter, paginationRequest);
    }

    /**
     * Retrieves suspended standing orders based on the provided pagination request.
     *
     * @param paginationRequest the pagination request containing details such as page number and size
     * @return a reactive Mono containing a paginated response of suspended standing orders
     */
    public Mono<PaginationResponse<TransactionLineStandingOrderDTO>> getSuspendedStandingOrders(
            PaginationRequest paginationRequest) {
        TransactionStandingOrderFilterDTO filter = TransactionStandingOrderFilterDTO.builder()
                .status(StandingOrderStatusEnum.ACTIVE)
                .isSuspended(true)
                .build();
        return searchStandingOrders(filter, paginationRequest);
    }

    /**
     * Retrieves the upcoming executions of transaction lines for standing orders
     * within the specified date range and supports pagination.
     *
     * @param fromDate the start date for filtering the next execution dates of standing orders
     * @param toDate the end date for filtering the next execution dates of standing orders
     * @param paginationRequest the pagination request object containing page size and offset details
     * @return a reactive Mono containing a paginated response of transaction line standing order DTOs
     */
    public Mono<PaginationResponse<TransactionLineStandingOrderDTO>> getUpcomingExecutions(
            LocalDate fromDate,
            LocalDate toDate,
            PaginationRequest paginationRequest) {
        TransactionStandingOrderFilterDTO filter = TransactionStandingOrderFilterDTO.builder()
                .status(StandingOrderStatusEnum.ACTIVE)
                .isSuspended(false)
                .nextExecutionDateFrom(fromDate)
                .nextExecutionDateTo(toDate)
                .build();
        return searchStandingOrders(filter, paginationRequest);
    }

    /**
     * Retrieves a paginated list of standing orders filtered by the recipient's name.
     *
     * @param recipientName the name of the recipient to filter standing orders by
     * @param paginationRequest the pagination details including page number and size
     * @return a {@code Mono} emitting a {@code PaginationResponse} containing the list of
     *         filtered {@code TransactionLineStandingOrderDTO}
     */
    public Mono<PaginationResponse<TransactionLineStandingOrderDTO>> getStandingOrdersByRecipient(
            String recipientName,
            PaginationRequest paginationRequest) {
        TransactionStandingOrderFilterDTO filter = TransactionStandingOrderFilterDTO.builder()
                .recipientName(recipientName)
                .status(StandingOrderStatusEnum.ACTIVE)
                .build();
        return searchStandingOrders(filter, paginationRequest);
    }

    /**
     * Retrieves the active standing orders associated with the specified IBAN.
     *
     * @param iban the International Bank Account Number to filter the standing orders
     * @param paginationRequest the pagination request containing page size and page number
     * @return a reactive Mono containing a paginated response of TransactionLineStandingOrderDTO objects
     */
    public Mono<PaginationResponse<TransactionLineStandingOrderDTO>> getStandingOrdersByIban(
            String iban,
            PaginationRequest paginationRequest) {
        TransactionStandingOrderFilterDTO filter = TransactionStandingOrderFilterDTO.builder()
                .recipientIbans(List.of(iban))
                .status(StandingOrderStatusEnum.ACTIVE)
                .build();
        return searchStandingOrders(filter, paginationRequest);
    }

    /**
     * Retrieves a paginated list of standing orders with a transaction amount exceeding the specified threshold.
     *
     * @param threshold the minimum transaction amount to filter standing orders
     * @param paginationRequest the details for pagination, including page number and size
     * @return a Mono containing a paginated response of standing orders that match the filter criteria
     */
    public Mono<PaginationResponse<TransactionLineStandingOrderDTO>> getHighValueStandingOrders(
            BigDecimal threshold,
            PaginationRequest paginationRequest) {
        TransactionStandingOrderFilterDTO filter = TransactionStandingOrderFilterDTO.builder()
                .minAmount(threshold)
                .status(StandingOrderStatusEnum.ACTIVE)
                .build();
        return searchStandingOrders(filter, paginationRequest);
    }

    /**
     * Retrieves a paginated list of expired standing orders based on the provided pagination request.
     * A standing order is considered expired if its end date is before or equal to the current date
     * and its status is active.
     *
     * @param paginationRequest the pagination request containing page number, size, and sorting details
     * @return a Mono emitting a paginated response with a list of expired standing orders
     */
    public Mono<PaginationResponse<TransactionLineStandingOrderDTO>> getExpiredStandingOrders(
            PaginationRequest paginationRequest) {
        TransactionStandingOrderFilterDTO filter = TransactionStandingOrderFilterDTO.builder()
                .endDateTo(LocalDate.now())
                .status(StandingOrderStatusEnum.ACTIVE)
                .build();
        return searchStandingOrders(filter, paginationRequest);
    }

    /**
     * Retrieves a paginated list of frequent executions of standing orders based on the specified minimum executions.
     *
     * @param minExecutions the minimum number of executions required for the standing orders to be included in the results
     * @param paginationRequest the pagination request containing page size and sorting information
     * @return a Mono emitting a paginated response containing the list of frequent standing order transactions
     */
    public Mono<PaginationResponse<TransactionLineStandingOrderDTO>> getFrequentExecutions(
            Integer minExecutions,
            PaginationRequest paginationRequest) {
        TransactionStandingOrderFilterDTO filter = TransactionStandingOrderFilterDTO.builder()
                .minTotalExecutions(minExecutions)
                .status(StandingOrderStatusEnum.ACTIVE)
                .build();
        return searchStandingOrders(filter, paginationRequest);
    }

    /**
     * Retrieves recently created standing orders based on the specified criteria.
     *
     * @param createdBy the identifier of the user who created the orders
     * @param fromDate the starting date to filter the orders by creation date
     * @param paginationRequest the pagination details to limit and sort the results
     * @return a {@code Mono} containing a paginated response of recently created
     *         standing order DTOs matching the filter criteria
     */
    public Mono<PaginationResponse<TransactionLineStandingOrderDTO>> getRecentlyCreatedOrders(
            String createdBy,
            LocalDate fromDate,
            PaginationRequest paginationRequest) {
        TransactionStandingOrderFilterDTO filter = TransactionStandingOrderFilterDTO.builder()
                .createdBy(createdBy)
                .startDateFrom(fromDate)
                .build();
        return searchStandingOrders(filter, paginationRequest);
    }

    /**
     * Retrieves the modified standing orders based on the provided filters.
     *
     * @param updatedBy the identifier of the user who updated the orders
     * @param fromDate the start date for filtering the modified orders
     * @param toDate the end date for filtering the modified orders
     * @param paginationRequest the pagination request containing page number and size information
     * @return a reactive Mono containing the paginated response of TransactionLineStandingOrderDTO objects
     */
    public Mono<PaginationResponse<TransactionLineStandingOrderDTO>> getModifiedOrders(
            String updatedBy,
            LocalDate fromDate,
            LocalDate toDate,
            PaginationRequest paginationRequest) {
        TransactionStandingOrderFilterDTO filter = TransactionStandingOrderFilterDTO.builder()
                .updatedBy(updatedBy)
                .startDateFrom(fromDate)
                .startDateTo(toDate)
                .build();
        return searchStandingOrders(filter, paginationRequest);
    }

    /**
     * Searches for standing orders based on multiple criteria such as recipient IBANs, frequency,
     * minimum amount, and whether to include expired standing orders. The result is paginated.
     *
     * @param recipientIbans a list of recipient IBANs to filter the standing orders
     * @param frequency the frequency of the standing orders to search for
     * @param minAmount the minimum amount for the standing orders
     * @param includeExpired a flag indicating whether to include expired standing orders
     * @param paginationRequest the pagination request containing page number and size
     * @return a paginated response containing the list of matching transaction standing orders
     */
    public Mono<PaginationResponse<TransactionLineStandingOrderDTO>> searchByMultipleCriteria(
            List<String> recipientIbans,
            StandingOrderFrequencyEnum frequency,
            BigDecimal minAmount,
            Boolean includeExpired,
            PaginationRequest paginationRequest) {
        TransactionStandingOrderFilterDTO filter = TransactionStandingOrderFilterDTO.builder()
                .recipientIbans(recipientIbans)
                .frequency(frequency)
                .minAmount(minAmount)
                .status(StandingOrderStatusEnum.ACTIVE)
                .endDateTo(includeExpired ? null : LocalDate.now())
                .build();
        return searchStandingOrders(filter, paginationRequest);
    }

}