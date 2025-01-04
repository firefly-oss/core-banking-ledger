package com.catalis.core.banking.ledger.web.controllers.core.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.common.web.error.models.ErrorResponse;
import com.catalis.core.banking.ledger.core.services.core.v1.TransactionCreateService;
import com.catalis.core.banking.ledger.core.services.core.v1.TransactionDeleteService;
import com.catalis.core.banking.ledger.core.services.core.v1.TransactionGetService;
import com.catalis.core.banking.ledger.core.services.core.v1.TransactionUpdateService;
import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionDTO;
import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionFilterDTO;
import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionSearchRequest;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/transactions")
@Tag(name = "Transaction Management", description = "Operations for managing transactions in the Core Banking system")
public class TransactionController {

    @Autowired
    private TransactionCreateService createService;

    @Autowired
    private TransactionGetService getService;

    @Autowired
    private TransactionUpdateService updateService;

    @Autowired
    private TransactionDeleteService deleteService;

    @Operation(summary = "Create a new transaction", description = "Creates a new transaction with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction successfully created",
                    content = @Content(schema = @Schema(implementation = TransactionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input provided",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public Mono<ResponseEntity<TransactionDTO>> createTransaction(
            @RequestBody @Valid TransactionDTO requestTransactionDTO) {
        return createService.createTransaction(requestTransactionDTO)
                .map(created -> ResponseEntity.status(201).body(created));
    }

    @Operation(summary = "Retrieve a transaction by ID", description = "Fetches a transaction using its unique identifier (ID)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction successfully found",
                    content = @Content(schema = @Schema(implementation = TransactionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @GetMapping("/details/{transactionId}")
    public Mono<ResponseEntity<TransactionDTO>> getTransaction(
            @Parameter(description = "Unique identifier of the transaction", required = true)
            @PathVariable("transactionId") Long transactionId) {
        return getService.getTransaction(transactionId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Search for transactions", description = "Search transactions based on filters and pagination options")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Search executed successfully",
                    content = @Content(schema = @Schema(implementation = PaginationResponse.class))
            )
    })
    @PostMapping("/search")
    public Mono<PaginationResponse<TransactionDTO>> searchTransactions(
            @Parameter(description = "Filters and pagination options for searching transactions", required = true)
            @RequestBody @Valid TransactionSearchRequest searchRequest) {

        // Extract both filter and pagination objects from the wrapper
        TransactionFilterDTO filter = searchRequest.getTransactionFilter();
        PaginationRequest pagination = searchRequest.getPagination();

        return getService.searchTransactions(filter, pagination);
    }

    @Operation(summary = "Update an existing transaction", description = "Updates the details of a transaction identified by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction successfully updated",
                    content = @Content(schema = @Schema(implementation = TransactionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @PutMapping("/{transactionId}")
    public Mono<ResponseEntity<TransactionDTO>> updateTransaction(
            @Parameter(description = "Unique identifier of the transaction to update", required = true)
            @PathVariable("transactionId") Long transactionId,

            @Parameter(
                    description = "Updated transaction details",
                    required = true,
                    schema = @Schema(implementation = TransactionDTO.class)
            )
            @RequestBody @Valid TransactionDTO requestTransactionDTO) {
        return updateService.updateTransaction(transactionId, requestTransactionDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a transaction", description = "Deletes a transaction identified by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Transaction successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @DeleteMapping("/{transactionId}")
    public Mono<ResponseEntity<Void>> deleteTransaction(
            @Parameter(description = "Unique identifier of the transaction to delete", required = true)
            @PathVariable("transactionId") Long transactionId) {
        return deleteService.deleteTransaction(transactionId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Retrieve transactions by account ID", description = "Fetches all transactions associated with a specific account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions successfully retrieved",
                    content = @Content(schema = @Schema(implementation = PaginationResponse.class)))
    })
    @GetMapping("/accounts/{accountId}")
    public Mono<PaginationResponse<TransactionDTO>> getTransactionsByAccount(
            @Parameter(description = "Unique identifier of the account", required = true)
            @PathVariable("accountId") Long accountId,

            @Parameter(
                    description = "Pagination and sorting options",
                    required = true,
                    schema = @Schema(implementation = PaginationRequest.class)
            )
            @RequestBody @Valid PaginationRequest paginationRequest) {
        return getService.getTransactionsByAccount(accountId, paginationRequest);
    }
}