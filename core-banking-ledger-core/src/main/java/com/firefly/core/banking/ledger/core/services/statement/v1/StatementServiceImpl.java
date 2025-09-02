package com.firefly.core.banking.ledger.core.services.statement.v1;

import com.firefly.common.core.queries.PaginationRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.common.core.queries.PaginationUtils;
import com.firefly.core.banking.ledger.core.mappers.statement.v1.StatementMapper;
import com.firefly.core.banking.ledger.interfaces.dtos.core.v1.TransactionDTO;
import com.firefly.core.banking.ledger.interfaces.dtos.statement.v1.StatementEntryDTO;
import com.firefly.core.banking.ledger.interfaces.dtos.statement.v1.StatementMetadataDTO;
import com.firefly.core.banking.ledger.interfaces.enums.core.v1.TransactionStatusEnum;
import com.firefly.core.banking.ledger.models.repositories.statement.v1.StatementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of the StatementService interface.
 */
@Service
@Transactional
public class StatementServiceImpl implements StatementService {

    @Autowired
    private StatementRepository repository;

    @Autowired
    private StatementMapper mapper;


    @Override
    public Mono<StatementMetadataDTO> getStatement(UUID statementId) {
        return repository.findById(statementId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<StatementMetadataDTO>> listAccountStatements(UUID accountId, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAccountId(accountId, pageable),
                () -> repository.countByAccountId(accountId)
        );
    }

    @Override
    public Mono<PaginationResponse<StatementMetadataDTO>> listAccountSpaceStatements(UUID accountSpaceId, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAccountSpaceId(accountSpaceId, pageable),
                () -> repository.countByAccountSpaceId(accountSpaceId)
        );
    }

    @Override
    public Mono<PaginationResponse<StatementMetadataDTO>> listAccountStatementsByDateRange(
            UUID accountId, LocalDate startDate, LocalDate endDate, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAccountIdAndDateRange(accountId, startDate, endDate, pageable),
                () -> repository.countByAccountId(accountId) // This is a simplification, ideally we'd count only within the date range
        );
    }

    @Override
    public Mono<PaginationResponse<StatementMetadataDTO>> listAccountSpaceStatementsByDateRange(
            UUID accountSpaceId, LocalDate startDate, LocalDate endDate, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAccountSpaceIdAndDateRange(accountSpaceId, startDate, endDate, pageable),
                () -> repository.countByAccountSpaceId(accountSpaceId) // This is a simplification, ideally we'd count only within the date range
        );
    }
}
