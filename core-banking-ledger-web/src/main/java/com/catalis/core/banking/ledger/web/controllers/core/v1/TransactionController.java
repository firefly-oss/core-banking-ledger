package com.catalis.core.banking.ledger.web.controllers.core.v1;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.core.services.core.v1.TransactionServiceImpl;
import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionDTO;
import com.catalis.core.banking.ledger.interfaces.enums.core.v1.TransactionStatusEnum;
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

@Tag(name = "Transactions", description = "APIs for managing transaction records in the ledger system")
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    @Autowired
    private TransactionServiceImpl service;

    @Operation(
            summary = "Create Transaction",
            description = "Create a new transaction record in the ledger."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid transaction data provided",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TransactionDTO>> createTransaction(
            @Parameter(description = "Transaction data to be created", required = true,
                    schema = @Schema(implementation = TransactionDTO.class))
            @RequestBody TransactionDTO transactionDTO
    ) {
        return service.createTransaction(transactionDTO)
                .map(createdTxn -> ResponseEntity.status(201).body(createdTxn))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Filter Transactions",
            description = "Apply custom filters to retrieve a paginated list of transactions from the ledger."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved filtered transactions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "No filtered results found",
                    content = @Content)
    })
    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<TransactionDTO>>> filterProducts(
            @ParameterObject
            @ModelAttribute FilterRequest<TransactionDTO> filterRequest
    ) {
        return service.filterTransactions(filterRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Get Transaction by ID",
            description = "Retrieve a specific transaction record by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the transaction",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found",
                    content = @Content)
    })
    @GetMapping(value = "/{transactionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TransactionDTO>> getTransaction(
            @Parameter(description = "Unique identifier of the transaction to retrieve", required = true)
            @PathVariable Long transactionId
    ) {
        return service.getTransaction(transactionId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Update Transaction",
            description = "Update an existing transaction record by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found",
                    content = @Content)
    })
    @PutMapping(value = "/{transactionId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TransactionDTO>> updateTransaction(
            @Parameter(description = "Unique identifier of the transaction to update", required = true)
            @PathVariable Long transactionId,

            @Parameter(description = "Updated transaction data", required = true,
                    schema = @Schema(implementation = TransactionDTO.class))
            @RequestBody TransactionDTO transactionDTO
    ) {
        return service.updateTransaction(transactionId, transactionDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete Transaction",
            description = "Remove an existing transaction record from the ledger by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Transaction deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Transaction not found",
                    content = @Content)
    })
    @DeleteMapping(value = "/{transactionId}")
    public Mono<ResponseEntity<Void>> deleteTransaction(
            @Parameter(description = "Unique identifier of the transaction to delete", required = true)
            @PathVariable Long transactionId
    ) {
        return service.deleteTransaction(transactionId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

    @Operation(
            summary = "Update Transaction Status",
            description = "Update the status of an existing transaction and record the status change in the history."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction status updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found",
                    content = @Content)
    })
    @PatchMapping(value = "/{transactionId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TransactionDTO>> updateTransactionStatus(
            @Parameter(description = "Unique identifier of the transaction to update", required = true)
            @PathVariable Long transactionId,

            @Parameter(description = "New status to set", required = true)
            @RequestParam TransactionStatusEnum newStatus,

            @Parameter(description = "Reason for the status change", required = true)
            @RequestParam String reason
    ) {
        return service.updateTransactionStatus(transactionId, newStatus, reason)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create Reversal Transaction",
            description = "Create a reversal transaction for an existing transaction."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reversal transaction created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Original transaction not found",
                    content = @Content)
    })
    @PostMapping(value = "/{transactionId}/reversal", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TransactionDTO>> createReversalTransaction(
            @Parameter(description = "Unique identifier of the original transaction", required = true)
            @PathVariable Long transactionId,

            @Parameter(description = "Reason for the reversal", required = true)
            @RequestParam String reason
    ) {
        return service.createReversalTransaction(transactionId, reason)
                .map(createdTxn -> ResponseEntity.status(201).body(createdTxn))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Find Transaction by External Reference",
            description = "Find a transaction by its external reference."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found",
                    content = @Content)
    })
    @GetMapping(value = "/by-reference/{externalReference}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TransactionDTO>> findByExternalReference(
            @Parameter(description = "External reference of the transaction", required = true)
            @PathVariable String externalReference
    ) {
        return service.findByExternalReference(externalReference)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}