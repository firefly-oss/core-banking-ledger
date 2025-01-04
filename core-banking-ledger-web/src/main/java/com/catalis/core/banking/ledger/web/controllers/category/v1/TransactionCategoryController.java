package com.catalis.core.banking.ledger.web.controllers.category.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.core.services.category.v1.TransactionCategoryCreateService;
import com.catalis.core.banking.ledger.core.services.category.v1.TransactionCategoryDeleteService;
import com.catalis.core.banking.ledger.core.services.category.v1.TransactionCategoryGetService;
import com.catalis.core.banking.ledger.core.services.category.v1.TransactionCategoryUpdateService;
import com.catalis.core.banking.ledger.interfaces.dtos.category.v1.TransactionCategoryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Transaction Categories", description = "Transaction category management operations")
public class TransactionCategoryController {

    @Autowired
    private TransactionCategoryCreateService createService;

    @Autowired
    private TransactionCategoryGetService getService;

    @Autowired
    private TransactionCategoryUpdateService updateService;

    @Autowired
    private TransactionCategoryDeleteService deleteService;

    @Operation(summary = "Create a new transaction category", description = "Creates a new transaction category and returns the created category details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction category successfully created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionCategoryDTO.class)))
    })
    @PostMapping("/")
    public Mono<ResponseEntity<TransactionCategoryDTO>> createCategory(
            @RequestBody @Valid TransactionCategoryDTO transactionCategoryDTO) {
        return createService.createTransactionCategory(transactionCategoryDTO)
                .map(createdTransactionCategory -> ResponseEntity.status(201).body(createdTransactionCategory));
    }

    @Operation(summary = "Retrieve transaction category by ID", description = "Fetches a transaction category by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction category retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionCategoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Transaction category not found")
    })
    @GetMapping("/details/{categoryId}")
    public Mono<ResponseEntity<TransactionCategoryDTO>> getCategory(@PathVariable Long categoryId) {
        return getService.getCategory(categoryId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Retrieve all transaction categories with pagination", description = "Returns a paginated list of all transaction categories.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction categories retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class)))
    })
    @GetMapping
    public Mono<PaginationResponse<TransactionCategoryDTO>> getAllCategories(
            @Valid PaginationRequest paginationRequest) {
        return getService.getAllCategories(paginationRequest);
    }

    @Operation(summary = "Update an existing transaction category", description = "Updates the details of an existing transaction category.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction category updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionCategoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Transaction category not found")
    })
    @PutMapping("/{categoryId}")
    public Mono<ResponseEntity<TransactionCategoryDTO>> updateCategory(
            @PathVariable Long categoryId,
            @RequestBody @Valid TransactionCategoryDTO transactionCategoryDTO) {
        return updateService.updateTransactionCategory(categoryId, transactionCategoryDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a transaction category", description = "Deletes a transaction category by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Transaction category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Transaction category not found")
    })
    @DeleteMapping("/{categoryId}")
    public Mono<ResponseEntity<Void>> deleteCategory(@PathVariable Long categoryId) {
        return deleteService.deleteTransactionCategory(categoryId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Retrieve subcategories of a parent category", description = "Fetches a paginated list of subcategories for a given parent category.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subcategories retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class)))
    })
    @GetMapping("/parent/{parentCategoryId}")
    public Mono<PaginationResponse<TransactionCategoryDTO>> getSubcategories(
            @PathVariable Long parentCategoryId,
            @Valid PaginationRequest paginationRequest) {
        return getService.getSubcategories(parentCategoryId, paginationRequest);
    }

}