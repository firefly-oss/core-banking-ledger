package com.catalis.core.banking.ledger.web.controllers.directdebit.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.common.web.error.models.ErrorResponse;
import com.catalis.core.banking.ledger.core.services.directdebit.v1.TransactionLineDirectDebitCreateService;
import com.catalis.core.banking.ledger.core.services.directdebit.v1.TransactionLineDirectDebitDeleteService;
import com.catalis.core.banking.ledger.core.services.directdebit.v1.TransactionLineDirectDebitGetService;
import com.catalis.core.banking.ledger.core.services.directdebit.v1.TransactionLineDirectDebitUpdateService;
import com.catalis.core.banking.ledger.interfaces.dtos.directdebit.v1.TransactionDirectDebitFilterDTO;
import com.catalis.core.banking.ledger.interfaces.dtos.directdebit.v1.TransactionLineDirectDebitDTO;
import com.catalis.core.banking.ledger.interfaces.dtos.directdebit.v1.TransactionSearchRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/direct-debits")
@Tag(name = "Direct Debits", description = "Operations for managing direct debits")
public class TransactionLineDirectDebitController {

    @Autowired
    private TransactionLineDirectDebitCreateService createService;

    @Autowired
    private TransactionLineDirectDebitUpdateService updateService;

    @Autowired
    private TransactionLineDirectDebitDeleteService deleteService;

    @Autowired
    private TransactionLineDirectDebitGetService getService;

    @Operation(summary = "Create direct debit", description = "Creates a new direct debit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Direct debit created successfully",
                    content = @Content(schema = @Schema(implementation = TransactionLineDirectDebitDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input provided",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public Mono<ResponseEntity<TransactionLineDirectDebitDTO>> createDirectDebit(
            @RequestBody @Valid TransactionLineDirectDebitDTO directDebitDTO) {
        return createService.createTransactionLineDirectDebit(directDebitDTO)
                .map(created -> ResponseEntity.status(201).body(created));
    }

    @Operation(summary = "Get direct debit details", description = "Retrieves details of a specific direct debit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Direct debit found",
                    content = @Content(schema = @Schema(implementation = TransactionLineDirectDebitDTO.class))),
            @ApiResponse(responseCode = "404", description = "Direct debit not found")
    })
    @GetMapping("/details/{directDebitId}")
    public Mono<ResponseEntity<TransactionLineDirectDebitDTO>> getDirectDebit(
            @Parameter(description = "ID of the direct debit", required = true)
            @PathVariable Long directDebitId) {
        return getService.getDirectDebit(directDebitId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update direct debit", description = "Updates an existing direct debit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Direct debit updated successfully",
                    content = @Content(schema = @Schema(implementation = TransactionLineDirectDebitDTO.class))),
            @ApiResponse(responseCode = "404", description = "Direct debit not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{directDebitId}")
    public Mono<ResponseEntity<TransactionLineDirectDebitDTO>> updateDirectDebit(
            @Parameter(description = "ID of the direct debit to update", required = true)
            @PathVariable Long directDebitId,
            @RequestBody @Valid TransactionLineDirectDebitDTO directDebitDTO) {
        return updateService.updateTransactionLineDirectDebit(directDebitId, directDebitDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete direct debit", description = "Deletes a direct debit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Direct debit deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Direct debit not found")
    })
    @DeleteMapping("/{directDebitId}")
    public Mono<ResponseEntity<Void>> deleteDirectDebit(
            @Parameter(description = "ID of the direct debit to delete", required = true)
            @PathVariable Long directDebitId) {
        return deleteService.deleteTransactionLineDirectDebit(directDebitId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Search direct debits", description = "Search direct debits based on various criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully",
                    content = @Content(schema = @Schema(implementation = PaginationResponse.class)))
    })
    @PostMapping("/search")
    public Mono<PaginationResponse<TransactionLineDirectDebitDTO>> searchDirectDebits(
            @Parameter(description = "Search criteria and pagination options", required = true)
            @RequestBody @Valid TransactionSearchRequest searchRequest) {

        TransactionDirectDebitFilterDTO filter = searchRequest.getTransactionFilter();
        PaginationRequest pagination = searchRequest.getPagination();

        return getService.searchDirectDebits(filter, pagination);
    }

}