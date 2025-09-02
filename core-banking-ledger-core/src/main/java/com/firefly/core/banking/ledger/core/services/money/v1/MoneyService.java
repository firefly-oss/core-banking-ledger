package com.firefly.core.banking.ledger.core.services.money.v1;

import java.util.UUID;

import com.firefly.common.core.queries.PaginationRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.core.banking.ledger.interfaces.dtos.money.v1.MoneyDTO;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing money values.
 */
public interface MoneyService {
    /**
     * Create a new money value.
     *
     * @param moneyDTO The money data.
     * @return The created money value.
     */
    Mono<MoneyDTO> createMoney(MoneyDTO moneyDTO);

    /**
     * Get a specific money value by ID.
     *
     * @param moneyId The ID of the money value.
     * @return The money value.
     */
    Mono<MoneyDTO> getMoney(UUID moneyId);

    /**
     * List all money values.
     *
     * @param paginationRequest Pagination parameters.
     * @return A paginated list of money values.
     */
    Mono<PaginationResponse<MoneyDTO>> listMoney(PaginationRequest paginationRequest);

    /**
     * List all money values with a specific currency.
     *
     * @param currency The currency code.
     * @param paginationRequest Pagination parameters.
     * @return A paginated list of money values.
     */
    Mono<PaginationResponse<MoneyDTO>> listMoneyByCurrency(String currency, PaginationRequest paginationRequest);
}
