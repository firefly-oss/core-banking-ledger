package com.catalis.core.banking.ledger.web.controllers.sepa.v1;

import com.catalis.core.banking.ledger.core.services.sepa.v1.TransactionLineSepaTransferServiceImpl;
import com.catalis.core.banking.ledger.interfaces.dtos.sepa.v1.TransactionLineSepaTransferDTO;
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

@Tag(name = "Transaction Line SEPA Transfer", description = "APIs for managing SEPA transfer lines associated with a specific transaction")
@RestController
@RequestMapping("/api/v1/transactions/{transactionId}/line-sepa-transfer")
public class TransactionLineSepaTransferController {

    @Autowired
    private TransactionLineSepaTransferServiceImpl service;

    @Operation(
            summary = "Get SEPA Transfer Line",
            description = "Retrieve the SEPA transfer line record associated with the specified transaction."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the SEPA transfer line record",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionLineSepaTransferDTO.class))),
            @ApiResponse(responseCode = "404", description = "SEPA transfer line record not found for this transaction",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TransactionLineSepaTransferDTO>> getSepaTransferLine(
            @Parameter(description = "Unique identifier of the transaction", required = true)
            @PathVariable Long transactionId
    ) {
        return service.getSepaTransferLine(transactionId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create SEPA Transfer Line",
            description = "Create a new SEPA transfer line record associated with the specified transaction."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "SEPA transfer line record created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionLineSepaTransferDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid SEPA transfer line data provided",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TransactionLineSepaTransferDTO>> createSepaTransferLine(
            @Parameter(description = "Unique identifier of the transaction", required = true)
            @PathVariable Long transactionId,

            @Parameter(description = "Data for the new SEPA transfer line record", required = true,
                    schema = @Schema(implementation = TransactionLineSepaTransferDTO.class))
            @RequestBody TransactionLineSepaTransferDTO sepaDTO
    ) {
        return service.createSepaTransferLine(transactionId, sepaDTO)
                .map(createdLine -> ResponseEntity.status(201).body(createdLine))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Update SEPA Transfer Line",
            description = "Update an existing SEPA transfer line record associated with the specified transaction."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SEPA transfer line updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionLineSepaTransferDTO.class))),
            @ApiResponse(responseCode = "404", description = "SEPA transfer line record not found for this transaction",
                    content = @Content)
    })
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TransactionLineSepaTransferDTO>> updateSepaTransferLine(
            @Parameter(description = "Unique identifier of the transaction", required = true)
            @PathVariable Long transactionId,

            @Parameter(description = "Updated data for the SEPA transfer line record", required = true,
                    schema = @Schema(implementation = TransactionLineSepaTransferDTO.class))
            @RequestBody TransactionLineSepaTransferDTO sepaDTO
    ) {
        return service.updateSepaTransferLine(transactionId, sepaDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete SEPA Transfer Line",
            description = "Remove an existing SEPA transfer line record from the specified transaction."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "SEPA transfer line record deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "SEPA transfer line record not found for this transaction",
                    content = @Content)
    })
    @DeleteMapping
    public Mono<ResponseEntity<Void>> deleteSepaTransferLine(
            @Parameter(description = "Unique identifier of the transaction", required = true)
            @PathVariable Long transactionId
    ) {
        return service.deleteSepaTransferLine(transactionId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}