package com.catalis.core.banking.ledger.core.services.money.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.common.core.queries.PaginationUtils;
import com.catalis.core.banking.ledger.core.mappers.money.v1.MoneyMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.money.v1.MoneyDTO;
import com.catalis.core.banking.ledger.models.entities.money.v1.Money;
import com.catalis.core.banking.ledger.models.repositories.money.v1.MoneyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * Implementation of the MoneyService interface.
 */
@Service
@Transactional
public class MoneyServiceImpl implements MoneyService {

    @Autowired
    private MoneyRepository repository;

    @Autowired
    private MoneyMapper mapper;

    @Override
    public Mono<MoneyDTO> createMoney(MoneyDTO moneyDTO) {
        Money entity = mapper.toEntity(moneyDTO);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<MoneyDTO> getMoney(Long moneyId) {
        return repository.findById(moneyId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<MoneyDTO>> listMoney(PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findAllBy(pageable),
                repository::count
        );
    }

    @Override
    public Mono<PaginationResponse<MoneyDTO>> listMoneyByCurrency(String currency, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByCurrency(currency, pageable),
                () -> repository.countByCurrency(currency)
        );
    }
}
