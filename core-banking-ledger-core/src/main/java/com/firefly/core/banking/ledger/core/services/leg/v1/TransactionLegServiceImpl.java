package com.firefly.core.banking.ledger.core.services.leg.v1;

import com.firefly.common.core.queries.PaginationRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.common.core.queries.PaginationUtils;
import com.firefly.core.banking.ledger.core.mappers.leg.v1.TransactionLegMapper;
import com.firefly.core.banking.ledger.interfaces.dtos.leg.v1.TransactionLegDTO;
import com.firefly.core.banking.ledger.models.entities.leg.v1.TransactionLeg;
import com.firefly.core.banking.ledger.models.repositories.leg.v1.TransactionLegRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import java.util.UUID;
/**
 * Implementation of the TransactionLegService interface.
 */
@Service
@Transactional
public class TransactionLegServiceImpl implements TransactionLegService {

    @Autowired
    private TransactionLegRepository repository;

    @Autowired
    private TransactionLegMapper mapper;

    @Override
    public Mono<TransactionLegDTO> createTransactionLeg(UUID transactionId, TransactionLegDTO legDTO) {
        legDTO.setTransactionId(transactionId);
        TransactionLeg entity = mapper.toEntity(legDTO);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TransactionLegDTO> getTransactionLeg(UUID transactionId, UUID legId) {
        return repository.findById(legId)
                .filter(entity -> entity.getTransactionId().equals(transactionId))
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<TransactionLegDTO>> listTransactionLegs(UUID transactionId, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByTransactionId(transactionId, pageable),
                () -> repository.countByTransactionId(transactionId)
        );
    }

    @Override
    public Mono<PaginationResponse<TransactionLegDTO>> listAccountLegs(UUID accountId, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAccountId(accountId, pageable),
                () -> repository.countByAccountId(accountId)
        );
    }

    @Override
    public Mono<PaginationResponse<TransactionLegDTO>> listAccountLegsByDateRange(
            UUID accountId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            PaginationRequest paginationRequest
    ) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAccountIdAndBookingDateBetween(accountId, startDate, endDate, pageable),
                () -> repository.countByAccountIdAndBookingDateBetween(accountId, startDate, endDate)
        );
    }
}
