package com.catalis.core.banking.ledger.web.controllers.core.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.common.web.error.models.ErrorResponse;
import com.catalis.core.banking.ledger.core.services.core.v1.TransactionStatusHistoryCreateService;
import com.catalis.core.banking.ledger.core.services.core.v1.TransactionStatusHistoryDeleteService;
import com.catalis.core.banking.ledger.core.services.core.v1.TransactionStatusHistoryGetService;
import com.catalis.core.banking.ledger.core.services.core.v1.TransactionStatusHistoryUpdateService;
import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionStatusHistoryDTO;
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

@RestController
@RequestMapping("/api/v1/transactions/{transactionId}/status-history")
@Tag(name = "Transaction Status History", description = "Operations for managing transaction status history")
public class TransactionStatusHistoryController {

    @Autowired
    private TransactionStatusHistoryCreateService createService;

    @Autowired
    private TransactionStatusHistoryGetService getService;

    @Autowired
    private TransactionStatusHistoryUpdateService updateService;

    @Autowired
    private TransactionStatusHistoryDeleteService deleteService;

    @Operation(summary = "Get status history", description = "Retrieves the status history for a specific transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status history retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public Mono<PaginationResponse<TransactionStatusHistoryDTO>> getStatusHistory(
            @Parameter(description = "ID of the transaction", required = true)
            @PathVariable Long transactionId,
            @Valid PaginationRequest paginationRequest) {
        return getService.getStatusHistory(transactionId, paginationRequest);
    }

    @Operation(summary = "Add status history entry", description = "Adds a new status entry to the transaction's history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Status history entry created",
                    content = @Content(schema = @Schema(implementation = TransactionStatusHistoryDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public Mono<ResponseEntity<TransactionStatusHistoryDTO>> addStatusHistoryEntry(
            @Parameter(description = "ID of the transaction", required = true)
            @PathVariable Long transactionId,
            @RequestBody @Valid TransactionStatusHistoryDTO statusHistoryDTO) {
        return createService.addStatusHistoryEntry(transactionId, statusHistoryDTO)
                .map(created -> ResponseEntity.status(201).body(created));
    }

    @Operation(summary = "Update status history entry", description = "Updates an existing status history entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status history entry updated",
                    content = @Content(schema = @Schema(implementation = TransactionStatusHistoryDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Status history entry not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{statusHistoryId}")
    public Mono<ResponseEntity<TransactionStatusHistoryDTO>> updateStatusHistoryEntry(
            @Parameter(description = "ID of the status history entry", required = true)
            @PathVariable Long statusHistoryId,
            @RequestBody @Valid TransactionStatusHistoryDTO statusHistoryDTO) {
        return updateService.updateStatusHistoryEntry(statusHistoryId, statusHistoryDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete status history entry", description = "Deletes a specific status history entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Status history entry deleted"),
            @ApiResponse(responseCode = "404", description = "Status history entry not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{statusHistoryId}")
    public Mono<ResponseEntity<Void>> deleteStatusHistoryEntry(
            @Parameter(description = "ID of the status history entry", required = true)
            @PathVariable Long statusHistoryId) {
        return deleteService.deleteStatusHistoryEntry(statusHistoryId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}