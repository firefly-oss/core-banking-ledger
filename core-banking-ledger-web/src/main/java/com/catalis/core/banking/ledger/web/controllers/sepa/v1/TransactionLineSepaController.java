package com.catalis.core.banking.ledger.web.controllers.sepa.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.core.services.sepa.v1.TransactionLineSepaCreateService;
import com.catalis.core.banking.ledger.core.services.sepa.v1.TransactionLineSepaDeleteService;
import com.catalis.core.banking.ledger.core.services.sepa.v1.TransactionLineSepaGetService;
import com.catalis.core.banking.ledger.core.services.sepa.v1.TransactionLineSepaUpdateService;
import com.catalis.core.banking.ledger.interfaces.dtos.sepa.v1.TransactionLineSepaTransferDTO;
import com.catalis.core.banking.ledger.interfaces.dtos.sepa.v1.TransactionSearchRequest;
import com.catalis.core.banking.ledger.interfaces.dtos.sepa.v1.TransactionSepaFilterDTO;
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
@RequestMapping("/api/v1/sepa-transactions")
@Tag(name = "SEPA Transactions", description = "Operations related to SEPA transfers")
public class TransactionLineSepaController {

    @Autowired
    private TransactionLineSepaCreateService transactionCreationService;

    @Autowired
    private TransactionLineSepaGetService transactionRetrievalService;

    @Autowired
    private TransactionLineSepaUpdateService transactionUpdateService;

    @Autowired
    private TransactionLineSepaDeleteService transactionDeletionService;

    @Operation(summary = "Create a SEPA transfer transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "SEPA transfer successfully created",
                    content = @Content(schema = @Schema(implementation = TransactionLineSepaTransferDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input provided")
    })
    @PostMapping("/")
    public Mono<ResponseEntity<TransactionLineSepaTransferDTO>> createTransaction(
            @RequestBody @Valid TransactionLineSepaTransferDTO sepaTransferRequest) {
        return transactionCreationService.createTransactionLineSepaTransfer(sepaTransferRequest)
                .map(sepaTransfer -> ResponseEntity.status(201).body(sepaTransfer));
    }

    @Operation(summary = "Retrieve a SEPA transfer transaction by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SEPA transfer transaction found",
                    content = @Content(schema = @Schema(implementation = TransactionLineSepaTransferDTO.class))),
            @ApiResponse(responseCode = "404", description = "SEPA transfer transaction not found")
    })
    @GetMapping("/details/{transactionId}")
    public Mono<ResponseEntity<TransactionLineSepaTransferDTO>> getTransactionById(
            @PathVariable Long transactionId) {
        return transactionRetrievalService.getSepaTransfer(transactionId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Search for SEPA transfer transactions with filtering and pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results returned successfully",
                    content = @Content(schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid search request")
    })
    @PostMapping("/search")
    public Mono<PaginationResponse<TransactionLineSepaTransferDTO>> searchTransactions(
            @RequestBody @Valid TransactionSearchRequest searchRequest) {

        TransactionSepaFilterDTO searchFilter = searchRequest.getTransactionFilter();
        PaginationRequest paginationOptions = searchRequest.getPagination();

        return transactionRetrievalService.searchSepaTransfers(searchFilter, paginationOptions);
    }

    @Operation(summary = "Update a SEPA transfer transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SEPA transfer transaction successfully updated",
                    content = @Content(schema = @Schema(implementation = TransactionLineSepaTransferDTO.class))),
            @ApiResponse(responseCode = "404", description = "SEPA transfer transaction not found")
    })
    @PutMapping("/{transactionId}")
    public Mono<ResponseEntity<TransactionLineSepaTransferDTO>> updateTransaction(
            @PathVariable Long transactionId,
            @RequestBody @Valid TransactionLineSepaTransferDTO sepaTransferUpdateRequest) {
        return transactionUpdateService.updateTransactionLineSepaTransfer(transactionId, sepaTransferUpdateRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a SEPA transfer transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "SEPA transfer transaction successfully deleted"),
            @ApiResponse(responseCode = "404", description = "SEPA transfer transaction not found")
    })
    @DeleteMapping("/{transactionId}")
    public Mono<ResponseEntity<Void>> deleteTransaction(@PathVariable Long transactionId) {
        return transactionDeletionService.deleteTransactionLineSepa(transactionId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}