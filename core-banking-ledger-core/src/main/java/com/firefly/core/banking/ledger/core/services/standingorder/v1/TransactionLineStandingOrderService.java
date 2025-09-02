package com.firefly.core.banking.ledger.core.services.standingorder.v1;

import java.util.UUID;

import com.firefly.core.banking.ledger.interfaces.dtos.standingorder.v1.TransactionLineStandingOrderDTO;
import reactor.core.publisher.Mono;

public interface TransactionLineStandingOrderService {

    /**
     * Retrieve the standing order line for the specified transaction.
     */
    Mono<TransactionLineStandingOrderDTO> getStandingOrderLine(UUID transactionId);

    /**
     * Create a new standing order line record for a specified transaction.
     */
    Mono<TransactionLineStandingOrderDTO> createStandingOrderLine(UUID transactionId, TransactionLineStandingOrderDTO standingOrderDTO);

    /**
     * Update an existing standing order line for the specified transaction.
     */
    Mono<TransactionLineStandingOrderDTO> updateStandingOrderLine(UUID transactionId, TransactionLineStandingOrderDTO standingOrderDTO);

    /**
     * Delete the standing order line record for the specified transaction.
     */
    Mono<Void> deleteStandingOrderLine(UUID transactionId);
}
