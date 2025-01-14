package com.catalis.core.banking.ledger.core.services.category.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.common.core.queries.PaginationUtils;
import com.catalis.core.banking.ledger.core.mappers.category.v1.TransactionCategoryMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.category.v1.TransactionCategoryDTO;
import com.catalis.core.banking.ledger.models.entities.category.v1.TransactionCategory;
import com.catalis.core.banking.ledger.models.repositories.category.v1.TransactionCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TransactionCategoryServiceImpl implements TransactionCategoryService {

    @Autowired
    private TransactionCategoryRepository repository;

    @Autowired
    private TransactionCategoryMapper mapper;

    @Override
    public Mono<PaginationResponse<TransactionCategoryDTO>> listCategories(PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findAllBy(pageable),
                repository::count
        );
    }

    @Override
    public Mono<TransactionCategoryDTO> createCategory(TransactionCategoryDTO dto) {
        TransactionCategory entity = mapper.toEntity(dto);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TransactionCategoryDTO> getCategory(Long categoryId) {
        return repository.findById(categoryId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TransactionCategoryDTO> updateCategory(Long categoryId, TransactionCategoryDTO dto) {
        return repository.findById(categoryId)
                .flatMap(existingEntity -> {
                    existingEntity.setCategoryName(dto.getCategoryName());
                    existingEntity.setCategoryDescription(dto.getCategoryDescription());
                    existingEntity.setCategoryType(dto.getCategoryType());
                    existingEntity.setParentCategoryId(dto.getParentCategoryId());
                    existingEntity.setSpanishTaxCode(dto.getSpanishTaxCode());
                    return repository.save(existingEntity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteCategory(Long categoryId) {
        return repository.deleteById(categoryId);
    }
}
