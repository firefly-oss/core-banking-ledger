package com.catalis.core.banking.ledger.web.controllers.interest.v1;

import com.catalis.core.banking.ledger.core.services.interest.v1.TransactionLineInterestServiceImpl;
import com.catalis.core.banking.ledger.interfaces.dtos.interest.v1.TransactionLineInterestDTO;
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

@Tag(name = "Transaction Line Interest", description = "APIs for managing interest lines associated with a specific transaction")
@RestController
@RequestMapping("/api/v1/transactions/{transactionId}/line-interest")
public class TransactionLineInterestController {

    @Autowired
    private TransactionLineInterestServiceImpl service;

    @Operation(
            summary = "Get Interest Line",
            description = "Retrieve the interest line record associated with the specified transaction."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the interest line",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionLineInterestDTO.class))),
            @ApiResponse(responseCode = "404", description = "Interest line not found for this transaction",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TransactionLineInterestDTO>> getInterestLine(
            @Parameter(description = "Unique identifier of the transaction", required = true)
            @PathVariable Long transactionId
    ) {
        return service.getInterestLine(transactionId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create Interest Line",
            description = "Create a new interest line record associated with the specified transaction."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Interest line record created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionLineInterestDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid interest line data provided",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TransactionLineInterestDTO>> createInterestLine(
            @Parameter(description = "Unique identifier of the transaction", required = true)
            @PathVariable Long transactionId,

            @Parameter(description = "Data for the new interest line record", required = true,
                    schema = @Schema(implementation = TransactionLineInterestDTO.class))
            @RequestBody TransactionLineInterestDTO interestDTO
    ) {
        return service.createInterestLine(transactionId, interestDTO)
                .map(createdLine -> ResponseEntity.status(201).body(createdLine))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Update Interest Line",
            description = "Update an existing interest line record for the specified transaction."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Interest line record updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionLineInterestDTO.class))),
            @ApiResponse(responseCode = "404", description = "Interest line record not found for this transaction",
                    content = @Content)
    })
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TransactionLineInterestDTO>> updateInterestLine(
            @Parameter(description = "Unique identifier of the transaction", required = true)
            @PathVariable Long transactionId,

            @Parameter(description = "Updated data for the interest line record", required = true,
                    schema = @Schema(implementation = TransactionLineInterestDTO.class))
            @RequestBody TransactionLineInterestDTO interestDTO
    ) {
        return service.updateInterestLine(transactionId, interestDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete Interest Line",
            description = "Remove an existing interest line record from the specified transaction."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Interest line record deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Interest line record not found for this transaction",
                    content = @Content)
    })
    @DeleteMapping
    public Mono<ResponseEntity<Void>> deleteInterestLine(
            @Parameter(description = "Unique identifier of the transaction", required = true)
            @PathVariable Long transactionId
    ) {
        return service.deleteInterestLine(transactionId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
