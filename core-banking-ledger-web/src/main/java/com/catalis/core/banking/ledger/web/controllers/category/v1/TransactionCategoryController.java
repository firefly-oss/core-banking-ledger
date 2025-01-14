package com.catalis.core.banking.ledger.web.controllers.category.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.core.services.category.v1.TransactionCategoryServiceImpl;
import com.catalis.core.banking.ledger.interfaces.dtos.category.v1.TransactionCategoryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Tag(name = "Transaction Categories", description = "APIs for managing transaction category records in the banking ledger system")
@RestController
@RequestMapping("/api/v1/transaction-categories")
public class TransactionCategoryController {

    @Autowired
    private TransactionCategoryServiceImpl service;

    @Operation(
            summary = "List Transaction Categories",
            description = "Retrieve a paginated list of all transaction category records."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the transaction categories",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "No transaction categories found",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<TransactionCategoryDTO>>> getAllCategories(
            @ParameterObject
            @ModelAttribute PaginationRequest paginationRequest
    ) {
        return service.listCategories(paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create Transaction Category",
            description = "Create a new transaction category record."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionCategoryDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid category data provided",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TransactionCategoryDTO>> createCategory(
            @Parameter(description = "Transaction category data", required = true,
                    schema = @Schema(implementation = TransactionCategoryDTO.class))
            @RequestBody TransactionCategoryDTO dto
    ) {
        return service.createCategory(dto)
                .map(createdCategory -> ResponseEntity.status(201).body(createdCategory))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get Transaction Category by ID",
            description = "Retrieve a specific transaction category by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the transaction category",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionCategoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Transaction category not found",
                    content = @Content)
    })
    @GetMapping(value = "/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TransactionCategoryDTO>> getCategory(
            @Parameter(description = "Unique identifier of the transaction category", required = true)
            @PathVariable Long categoryId
    ) {
        return service.getCategory(categoryId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Update Transaction Category",
            description = "Update an existing transaction category record by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction category updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionCategoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Transaction category not found",
                    content = @Content)
    })
    @PutMapping(value = "/{categoryId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TransactionCategoryDTO>> updateCategory(
            @Parameter(description = "Unique identifier of the transaction category to update", required = true)
            @PathVariable Long categoryId,

            @Parameter(description = "Updated transaction category data", required = true,
                    schema = @Schema(implementation = TransactionCategoryDTO.class))
            @RequestBody TransactionCategoryDTO dto
    ) {
        return service.updateCategory(categoryId, dto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete Transaction Category",
            description = "Remove an existing transaction category record by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Transaction category deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Transaction category not found",
                    content = @Content)
    })
    @DeleteMapping(value = "/{categoryId}")
    public Mono<ResponseEntity<Void>> deleteCategory(
            @Parameter(description = "Unique identifier of the transaction category to delete", required = true)
            @PathVariable Long categoryId
    ) {
        return service.deleteCategory(categoryId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}