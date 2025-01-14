package com.catalis.core.banking.ledger.core.services.ledger.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.common.core.queries.PaginationUtils;
import com.catalis.core.banking.ledger.core.mappers.ledger.v1.LedgerEntryMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.ledger.v1.LedgerEntryDTO;
import com.catalis.core.banking.ledger.models.entities.ledger.v1.LedgerEntry;
import com.catalis.core.banking.ledger.models.repositories.ledger.v1.LedgerEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class LedgerEntryServiceImpl implements LedgerEntryService {

    @Autowired
    private LedgerEntryRepository repository;

    @Autowired
    private LedgerEntryMapper mapper;

    @Override
    public Mono<PaginationResponse<LedgerEntryDTO>> listLedgerEntries(
            Long transactionId, Long ledgerAccountId, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByTransactionIdAndLedgerAccountId(transactionId, ledgerAccountId, pageable),
                () -> repository.countByTransactionIdAndLedgerAccountId(transactionId, ledgerAccountId)
        );
    }

    @Override
    public Mono<LedgerEntryDTO> createLedgerEntry(LedgerEntryDTO dto) {
        LedgerEntry entity = mapper.toEntity(dto);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<LedgerEntryDTO> getLedgerEntry(Long ledgerEntryId) {
        return repository.findById(ledgerEntryId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<LedgerEntryDTO> updateLedgerEntry(Long ledgerEntryId, LedgerEntryDTO dto) {
        return repository.findById(ledgerEntryId)
                .flatMap(existingEntity -> {
                    LedgerEntry updatedEntity = mapper.toEntity(dto);
                    updatedEntity.setLedgerEntryId(existingEntity.getLedgerEntryId());
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteLedgerEntry(Long ledgerEntryId) {
        return repository.deleteById(ledgerEntryId);
    }
}
