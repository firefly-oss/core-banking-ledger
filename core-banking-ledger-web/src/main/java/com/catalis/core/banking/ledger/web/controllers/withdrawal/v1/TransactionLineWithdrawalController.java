package com.catalis.core.banking.ledger.web.controllers.withdrawal.v1;

import com.catalis.core.banking.ledger.core.services.withdrawal.v1.TransactionLineWithdrawalServiceImpl;
import com.catalis.core.banking.ledger.interfaces.dtos.withdrawal.v1.TransactionLineWithdrawalDTO;
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

@Tag(name = "Transaction Line Withdrawal", description = "APIs for managing withdrawal lines associated with a specific transaction")
@RestController
@RequestMapping("/api/v1/transactions/{transactionId}/line-withdrawal")
public class TransactionLineWithdrawalController {

    @Autowired
    private TransactionLineWithdrawalServiceImpl service;

    @Operation(
            summary = "Get Withdrawal Line",
            description = "Retrieve the withdrawal line record associated with the specified transaction."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the withdrawal line",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionLineWithdrawalDTO.class))),
            @ApiResponse(responseCode = "404", description = "Withdrawal line not found for this transaction",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TransactionLineWithdrawalDTO>> getWithdrawalLine(
            @Parameter(description = "Unique identifier of the transaction", required = true)
            @PathVariable Long transactionId
    ) {
        return service.getWithdrawalLine(transactionId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create Withdrawal Line",
            description = "Create a new withdrawal line record associated with the specified transaction."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Withdrawal line record created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionLineWithdrawalDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid withdrawal line data provided",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TransactionLineWithdrawalDTO>> createWithdrawalLine(
            @Parameter(description = "Unique identifier of the transaction", required = true)
            @PathVariable Long transactionId,

            @Parameter(description = "Data for the new withdrawal line record", required = true,
                    schema = @Schema(implementation = TransactionLineWithdrawalDTO.class))
            @RequestBody TransactionLineWithdrawalDTO withdrawalDTO
    ) {
        return service.createWithdrawalLine(transactionId, withdrawalDTO)
                .map(createdLine -> ResponseEntity.status(201).body(createdLine))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Update Withdrawal Line",
            description = "Update an existing withdrawal line record for the specified transaction."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Withdrawal line record updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionLineWithdrawalDTO.class))),
            @ApiResponse(responseCode = "404", description = "Withdrawal line record not found for this transaction",
                    content = @Content)
    })
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TransactionLineWithdrawalDTO>> updateWithdrawalLine(
            @Parameter(description = "Unique identifier of the transaction", required = true)
            @PathVariable Long transactionId,

            @Parameter(description = "Updated data for the withdrawal line record", required = true,
                    schema = @Schema(implementation = TransactionLineWithdrawalDTO.class))
            @RequestBody TransactionLineWithdrawalDTO withdrawalDTO
    ) {
        return service.updateWithdrawalLine(transactionId, withdrawalDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete Withdrawal Line",
            description = "Remove an existing withdrawal line record from the specified transaction."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Withdrawal line record deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Withdrawal line record not found for this transaction",
                    content = @Content)
    })
    @DeleteMapping
    public Mono<ResponseEntity<Void>> deleteWithdrawalLine(
            @Parameter(description = "Unique identifier of the transaction", required = true)
            @PathVariable Long transactionId
    ) {
        return service.deleteWithdrawalLine(transactionId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
