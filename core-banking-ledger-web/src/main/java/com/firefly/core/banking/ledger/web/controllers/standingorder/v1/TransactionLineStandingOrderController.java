package com.firefly.core.banking.ledger.web.controllers.standingorder.v1;

import com.firefly.core.banking.ledger.core.services.standingorder.v1.TransactionLineStandingOrderServiceImpl;
import com.firefly.core.banking.ledger.interfaces.dtos.standingorder.v1.TransactionLineStandingOrderDTO;
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

@Tag(name = "Transaction Line Standing Order", description = "APIs for managing standing order line records associated with a specific transaction")
@RestController
@RequestMapping("/api/v1/transactions/{transactionId}/line-standing-order")
public class TransactionLineStandingOrderController {

    @Autowired
    private TransactionLineStandingOrderServiceImpl service;

    @Operation(
            summary = "Get Standing Order Line",
            description = "Retrieve the standing order line record associated with the specified transaction."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the standing order line record",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionLineStandingOrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Standing order line not found for this transaction",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TransactionLineStandingOrderDTO>> getStandingOrderLine(
            @Parameter(description = "Unique identifier of the transaction", required = true)
            @PathVariable Long transactionId
    ) {
        return service.getStandingOrderLine(transactionId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create Standing Order Line",
            description = "Create a new standing order line record associated with the specified transaction."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Standing order line record created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionLineStandingOrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid standing order line data provided",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TransactionLineStandingOrderDTO>> createStandingOrderLine(
            @Parameter(description = "Unique identifier of the transaction", required = true)
            @PathVariable Long transactionId,

            @Parameter(description = "Data for the new standing order line record", required = true,
                    schema = @Schema(implementation = TransactionLineStandingOrderDTO.class))
            @RequestBody TransactionLineStandingOrderDTO standingOrderDTO
    ) {
        return service.createStandingOrderLine(transactionId, standingOrderDTO)
                .map(createdLine -> ResponseEntity.status(201).body(createdLine))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Update Standing Order Line",
            description = "Update an existing standing order line record associated with the specified transaction."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Standing order line updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionLineStandingOrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Standing order line record not found for this transaction",
                    content = @Content)
    })
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TransactionLineStandingOrderDTO>> updateStandingOrderLine(
            @Parameter(description = "Unique identifier of the transaction", required = true)
            @PathVariable Long transactionId,

            @Parameter(description = "Updated data for the standing order line record", required = true,
                    schema = @Schema(implementation = TransactionLineStandingOrderDTO.class))
            @RequestBody TransactionLineStandingOrderDTO standingOrderDTO
    ) {
        return service.updateStandingOrderLine(transactionId, standingOrderDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete Standing Order Line",
            description = "Remove an existing standing order line record from the specified transaction."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Standing order line record deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Standing order line record not found for this transaction",
                    content = @Content)
    })
    @DeleteMapping
    public Mono<ResponseEntity<Void>> deleteStandingOrderLine(
            @Parameter(description = "Unique identifier of the transaction", required = true)
            @PathVariable Long transactionId
    ) {
        return service.deleteStandingOrderLine(transactionId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}