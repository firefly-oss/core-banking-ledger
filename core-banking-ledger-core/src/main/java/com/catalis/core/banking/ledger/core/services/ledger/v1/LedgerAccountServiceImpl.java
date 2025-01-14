package com.catalis.core.banking.ledger.core.services.ledger.v1;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.common.core.queries.PaginationUtils;
import com.catalis.core.banking.ledger.core.mappers.ledger.v1.LedgerAccountMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.ledger.v1.LedgerAccountDTO;
import com.catalis.core.banking.ledger.models.entities.ledger.v1.LedgerAccount;
import com.catalis.core.banking.ledger.models.repositories.ledger.v1.LedgerAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class LedgerAccountServiceImpl implements LedgerAccountService {

    @Autowired
    private LedgerAccountRepository repository;

    @Autowired
    private LedgerAccountMapper mapper;

    @Override
    public Mono<PaginationResponse<LedgerAccountDTO>> listLedgerAccounts(PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findAllBy(pageable),
                repository::count
        );
    }

    @Override
    public Mono<PaginationResponse<LedgerAccountDTO>> filterLedgerAccounts(FilterRequest<LedgerAccountDTO> filterRequest) {
        return FilterUtils
                .createFilter(
                        LedgerAccount.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    @Override
    public Mono<LedgerAccountDTO> createLedgerAccount(LedgerAccountDTO dto) {
        return Mono.just(dto)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<LedgerAccountDTO> getLedgerAccount(Long ledgerAccountId) {
        return repository.findById(ledgerAccountId)
                .map(mapper::toDTO)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Ledger Account not found")));
    }

    @Override
    public Mono<LedgerAccountDTO> updateLedgerAccount(Long ledgerAccountId, LedgerAccountDTO dto) {
        return repository.findById(ledgerAccountId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Ledger Account not found")))
                .flatMap(existingEntity -> {
                    existingEntity.setAccountCode(dto.getAccountCode());
                    existingEntity.setAccountName(dto.getAccountName());
                    existingEntity.setAccountType(dto.getAccountType());
                    existingEntity.setParentAccountId(dto.getParentAccountId());
                    existingEntity.setIsActive(dto.getIsActive());
                    return repository.save(existingEntity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteLedgerAccount(Long ledgerAccountId) {
        return repository.findById(ledgerAccountId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Ledger Account not found")))
                .flatMap(repository::delete);
    }
}
