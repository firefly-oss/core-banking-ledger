package com.catalis.core.banking.ledger.core.services.card.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.common.core.queries.PaginationUtils;
import com.catalis.core.banking.ledger.core.mappers.card.v1.TransactionLineCardMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.card.v1.TransactionCardFilterDTO;
import com.catalis.core.banking.ledger.interfaces.dtos.card.v1.TransactionLineCardDTO;
import com.catalis.core.banking.ledger.models.repositories.card.v1.TransactionLineCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional(readOnly = true)
public class TransactionLineCardGetService {

    @Autowired
    private TransactionLineCardRepository repository;

    @Autowired
    private TransactionLineCardMapper mapper;

    /**
     * Retrieves a card transaction by its unique identifier.
     *
     * @param id the unique identifier of the card transaction to retrieve.
     * @return a Mono containing the TransactionLineCardDTO if found, or an empty Mono if not found.
     */
    public Mono<TransactionLineCardDTO> getCardTransaction(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO);
    }

    /**
     * Retrieves a TransactionLineCardDTO by the specified transaction ID.
     *
     * @param transactionId the unique identifier of the transaction to retrieve
     * @return a Mono containing the TransactionLineCardDTO if found, or an empty Mono if not found
     */
    public Mono<TransactionLineCardDTO> getByTransactionId(Long transactionId) {
        return repository.findByTransactionId(transactionId)
                .map(mapper::toDTO);
    }

    /**
     * Retrieves a paginated list of card transactions based on the specified filter criteria.
     *
     * @param filter the filter criteria used to search for card transactions.
     *               It contains parameters such as date range, merchant codes,
     *               card presence, minimum and maximum transaction amounts.
     * @param paginationRequest an object containing pagination details
     *                          such as page number and page size.
     * @return a Mono containing a paginated response with the list of
     *         card transactions matching the filter criteria.
     */
    public Mono<PaginationResponse<TransactionLineCardDTO>> searchCardTransactions(
            TransactionCardFilterDTO filter,
            PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByCustomCriteria(
                        filter.getStartDate(),
                        filter.getEndDate(),
                        filter.getMerchantCodes() != null && !filter.getMerchantCodes().isEmpty()
                                ? filter.getMerchantCodes().get(0)
                                : null,
                        filter.getCardPresentOnly(),
                        filter.getMinAmount(),
                        filter.getMaxAmount(),
                        pageable
                ),
                () -> repository.countByCustomCriteria(
                        filter.getStartDate(),
                        filter.getEndDate(),
                        filter.getMerchantCodes() != null && !filter.getMerchantCodes().isEmpty()
                                ? filter.getMerchantCodes().get(0)
                                : null,
                        filter.getCardPresentOnly(),
                        filter.getMinAmount(),
                        filter.getMaxAmount()
                )
        );
    }


    /**
     * Retrieves a paginated list of transactions that have been flagged as fraudulent.
     *
     * @param paginationRequest the pagination request containing details such as page size and page number.
     * @return a {@code Mono} emitting a {@code PaginationResponse} containing a list of {@code TransactionLineCardDTO}
     *         objects that have the fraud flag set to true.
     */
    public Mono<PaginationResponse<TransactionLineCardDTO>> getFraudFlaggedTransactions(
            PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByCardFraudFlag(true, pageable),
                () -> repository.count()
        );
    }

}
