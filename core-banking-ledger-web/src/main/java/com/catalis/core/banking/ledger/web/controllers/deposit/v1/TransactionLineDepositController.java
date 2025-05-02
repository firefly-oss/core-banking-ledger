package com.catalis.core.banking.ledger.web.controllers.deposit.v1;

import com.catalis.core.banking.ledger.core.services.deposit.v1.TransactionLineDepositServiceImpl;
import com.catalis.core.banking.ledger.interfaces.dtos.deposit.v1.TransactionLineDepositDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Tag(name = "Transaction Line Deposit", description = "APIs for managing deposit lines associated with a specific transaction")
@RestController
@RequestMapping("/api/v1/transactions/{transactionId}/line-deposit")
public class TransactionLineDepositController {

    @Autowired
    private TransactionLineDepositServiceImpl service;

    @Operation(
            summary = "Get Deposit Line",
            description = "Retrieve the deposit line record associated with the specified transaction."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the deposit line",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionLineDepositDTO.class))),
            @ApiResponse(responseCode = "404", description = "Deposit line not found for this transaction",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TransactionLineDepositDTO>> getDepositLine(
            @Parameter(description = "Unique identifier of the transaction", required = true)
            @PathVariable Long transactionId
    ) {
        return service.getDepositLine(transactionId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create Deposit Line",
            description = "Create a new deposit line record associated with the specified transaction."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Deposit line record created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionLineDepositDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid deposit line data provided",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TransactionLineDepositDTO>> createDepositLine(
            @Parameter(description = "Unique identifier of the transaction", required = true)
            @PathVariable Long transactionId,

            @Parameter(description = "Data for the new deposit line record", required = true,
                    schema = @Schema(implementation = TransactionLineDepositDTO.class))
            @RequestBody TransactionLineDepositDTO depositDTO
    ) {
        return service.createDepositLine(transactionId, depositDTO)
                .map(createdLine -> ResponseEntity.status(201).body(createdLine))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Update Deposit Line",
            description = "Update an existing deposit line record for the specified transaction."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deposit line record updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionLineDepositDTO.class))),
            @ApiResponse(responseCode = "404", description = "Deposit line record not found for this transaction",
                    content = @Content)
    })
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TransactionLineDepositDTO>> updateDepositLine(
            @Parameter(description = "Unique identifier of the transaction", required = true)
            @PathVariable Long transactionId,

            @Parameter(description = "Updated data for the deposit line record", required = true,
                    schema = @Schema(implementation = TransactionLineDepositDTO.class))
            @RequestBody TransactionLineDepositDTO depositDTO
    ) {
        return service.updateDepositLine(transactionId, depositDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete Deposit Line",
            description = "Remove an existing deposit line record from the specified transaction."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deposit line record deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Deposit line record not found for this transaction",
                    content = @Content)
    })
    @DeleteMapping
    public Mono<ResponseEntity<Void>> deleteDepositLine(
            @Parameter(description = "Unique identifier of the transaction", required = true)
            @PathVariable Long transactionId
    ) {
        return service.deleteDepositLine(transactionId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
