package com.firefly.core.banking.ledger.core.services.standingorder.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.standingorder.v1.TransactionLineStandingOrderDTO;
import reactor.core.publisher.Mono;

public interface TransactionLineStandingOrderService {

    /**
     * Retrieve the standing order line for the specified transaction.
     */
    Mono<TransactionLineStandingOrderDTO> getStandingOrderLine(Long transactionId);

    /**
     * Create a new standing order line record for a specified transaction.
     */
    Mono<TransactionLineStandingOrderDTO> createStandingOrderLine(Long transactionId, TransactionLineStandingOrderDTO standingOrderDTO);

    /**
     * Update an existing standing order line for the specified transaction.
     */
    Mono<TransactionLineStandingOrderDTO> updateStandingOrderLine(Long transactionId, TransactionLineStandingOrderDTO standingOrderDTO);

    /**
     * Delete the standing order line record for the specified transaction.
     */
    Mono<Void> deleteStandingOrderLine(Long transactionId);
}
