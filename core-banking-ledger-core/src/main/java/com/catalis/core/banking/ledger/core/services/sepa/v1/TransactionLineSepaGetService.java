package com.catalis.core.banking.ledger.core.services.sepa.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.common.core.queries.PaginationUtils;
import com.catalis.core.banking.ledger.core.mappers.sepa.v1.TransactionLineSepaTransferMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.sepa.v1.TransactionLineSepaTransferDTO;
import com.catalis.core.banking.ledger.interfaces.dtos.sepa.v1.TransactionSepaFilterDTO;
import com.catalis.core.banking.ledger.models.repositories.sepa.v1.TransactionLineSepaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional(readOnly = true)
public class TransactionLineSepaGetService {

    @Autowired
    private TransactionLineSepaRepository repository;

    @Autowired
    private TransactionLineSepaTransferMapper mapper;

    /**
     * Retrieves a specific SEPA transfer record by its unique identifier.
     *
     * @param id the unique identifier of the SEPA transfer record to retrieve
     * @return a {@link Mono} emitting the {@link TransactionLineSepaTransferDTO} representing the SEPA transfer record
     */
    public Mono<TransactionLineSepaTransferDTO> getSepaTransfer(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO);
    }

    /**
     * Retrieves a TransactionLineSepaTransferDTO based on the given transaction ID.
     *
     * @param transactionId the unique identifier of the transaction to be retrieved
     * @return a Mono containing the TransactionLineSepaTransferDTO if found, or an empty Mono if not
     */
    public Mono<TransactionLineSepaTransferDTO> getByTransactionId(Long transactionId) {
        return repository.findByTransactionId(transactionId)
                .map(mapper::toDTO);
    }

    /**
     * Searches SEPA transfers based on the specified filter criteria and pagination details.
     *
     * @param filter the filter object containing criteria such as date range, SEPA statuses, creditor IDs,
     *               minimum and maximum amount, and other optional parameters for filtering SEPA transfers.
     * @param paginationRequest the pagination request object containing details such as page number
     *                          and page size for paginated results.
     * @return a reactive Mono containing the paginated response of TransactionLineSepaTransferDTO objects
     *         that match the specified filter and pagination details.
     */
    public Mono<PaginationResponse<TransactionLineSepaTransferDTO>> searchSepaTransfers(
            TransactionSepaFilterDTO filter,
            PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByCustomCriteria(
                        filter.getStartDate(),
                        filter.getEndDate(),
                        filter.getSepaStatuses() != null && !filter.getSepaStatuses().isEmpty()
                                ? filter.getSepaStatuses().get(0)
                                : null,
                        filter.getCreditorIds() != null && !filter.getCreditorIds().isEmpty()
                                ? filter.getCreditorIds().get(0)
                                : null,
                        filter.getMinAmount(),
                        filter.getMaxAmount(),
                        pageable
                ),
                () -> repository.countByCustomCriteria(
                        filter.getStartDate(),
                        filter.getEndDate(),
                        filter.getSepaStatuses() != null && !filter.getSepaStatuses().isEmpty()
                                ? filter.getSepaStatuses().get(0)
                                : null,
                        filter.getCreditorIds() != null && !filter.getCreditorIds().isEmpty()
                                ? filter.getCreditorIds().get(0)
                                : null,
                        filter.getMinAmount(),
                        filter.getMaxAmount()
                )
        );
    }

}
