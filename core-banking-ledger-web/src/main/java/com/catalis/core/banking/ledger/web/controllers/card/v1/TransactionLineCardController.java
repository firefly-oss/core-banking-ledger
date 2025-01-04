package com.catalis.core.banking.ledger.web.controllers.card.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.core.services.card.v1.TransactionLineCardCreateService;
import com.catalis.core.banking.ledger.core.services.card.v1.TransactionLineCardDeleteService;
import com.catalis.core.banking.ledger.core.services.card.v1.TransactionLineCardGetService;
import com.catalis.core.banking.ledger.core.services.card.v1.TransactionLineCardUpdateService;
import com.catalis.core.banking.ledger.interfaces.dtos.card.v1.TransactionCardFilterDTO;
import com.catalis.core.banking.ledger.interfaces.dtos.card.v1.TransactionLineCardDTO;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/v1/card-transactions")
@Tag(name = "Card Transactions", description = "Card transaction management operations")
public class TransactionLineCardController {

    @Autowired
    private TransactionLineCardCreateService createService;

    @Autowired
    private TransactionLineCardGetService getService;

    @Autowired
    private TransactionLineCardUpdateService updateService;

    @Autowired
    private TransactionLineCardDeleteService deleteService;

    @Operation(summary = "Create a new card transaction", description = "This endpoint allows creating a new card transaction.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created the card transaction",
                    content = @Content(schema = @Schema(implementation = TransactionLineCardDTO.class)))
    })
    @PostMapping("/")
    public Mono<ResponseEntity<TransactionLineCardDTO>> createCardTransaction(
            @RequestBody @Valid TransactionLineCardDTO transactionLineCardDTO) {
        return createService.createTransactionLineCard(transactionLineCardDTO)
                .map(created -> ResponseEntity.status(201).body(created));
    }

    @Operation(summary = "Retrieve card transaction by ID", description = "This endpoint fetches a card transaction by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the card transaction"),
            @ApiResponse(responseCode = "404", description = "Card transaction with the given ID not found")
    })
    @GetMapping("/{transactionId}")
    public Mono<ResponseEntity<TransactionLineCardDTO>> getCardTransaction(@PathVariable Long transactionId) {
        return getService.getCardTransaction(transactionId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Search card transactions", description = "This endpoint allows searching card transactions based on filters and pagination.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved filtered and paginated results")
    })
    @PostMapping("/search")
    public Mono<PaginationResponse<TransactionLineCardDTO>> searchCardTransactions(
            @RequestBody @Valid TransactionCardFilterDTO transactionFilterDTO,
            @Valid PaginationRequest paginationRequest) {
        return getService.searchCardTransactions(transactionFilterDTO, paginationRequest);
    }

    @Operation(summary = "Update an existing card transaction", description = "This endpoint updates the details of an existing card transaction.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the card transaction"),
            @ApiResponse(responseCode = "404", description = "Card transaction with the given ID not found")
    })
    @PutMapping("/{transactionId}")
    public Mono<ResponseEntity<TransactionLineCardDTO>> updateCardTransaction(
            @PathVariable Long transactionId,
            @RequestBody @Valid TransactionLineCardDTO transactionLineCardDTO) {
        return updateService.updateTransactionLineCard(transactionId, transactionLineCardDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a card transaction", description = "This endpoint deletes a card transaction by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the card transaction"),
            @ApiResponse(responseCode = "404", description = "Card transaction with the given ID not found")
    })
    @DeleteMapping("/{transactionId}")
    public Mono<ResponseEntity<Void>> deleteCardTransaction(@PathVariable Long transactionId) {
        return deleteService.deleteTransactionLineCard(transactionId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
}
