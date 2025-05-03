package com.catalis.core.banking.ledger.web.controllers.leg.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.core.services.leg.v1.TransactionLegService;
import com.catalis.core.banking.ledger.interfaces.dtos.leg.v1.TransactionLegDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * REST controller for managing transaction legs.
 */
@RestController
@RequestMapping("/api/v1/transactions/{transactionId}/legs")
@Tag(name = "Transaction Legs", description = "API endpoints for managing transaction legs")
public class TransactionLegController {

    @Autowired
    private TransactionLegService service;

    @Operation(
            summary = "Create Transaction Leg",
            description = "Create a new transaction leg for a specific transaction."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Transaction leg created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionLegDTO.class))
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TransactionLegDTO>> createTransactionLeg(
            @Parameter(description = "Transaction ID", required = true)
            @PathVariable Long transactionId,
            
            @Parameter(description = "Transaction leg data", required = true,
                    schema = @Schema(implementation = TransactionLegDTO.class))
            @RequestBody TransactionLegDTO legDTO
    ) {
        return service.createTransactionLeg(transactionId, legDTO)
                .map(createdLeg -> ResponseEntity.status(201).body(createdLeg))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get Transaction Leg",
            description = "Retrieve a specific transaction leg by its ID."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Transaction leg retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionLegDTO.class))
    )
    @GetMapping(value = "/{legId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TransactionLegDTO>> getTransactionLeg(
            @Parameter(description = "Transaction ID", required = true)
            @PathVariable Long transactionId,
            
            @Parameter(description = "Transaction leg ID", required = true)
            @PathVariable Long legId
    ) {
        return service.getTransactionLeg(transactionId, legId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "List Transaction Legs",
            description = "Retrieve a paginated list of legs for a specific transaction."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Transaction legs retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<TransactionLegDTO>>> listTransactionLegs(
            @Parameter(description = "Transaction ID", required = true)
            @PathVariable Long transactionId,
            
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size,
            
            @Parameter(description = "Sort field")
            @RequestParam(required = false) String sort,
            
            @Parameter(description = "Sort direction (ASC or DESC)")
            @RequestParam(defaultValue = "DESC") String direction
    ) {
        PaginationRequest paginationRequest = PaginationRequest.of(page, size, sort, direction);
        return service.listTransactionLegs(transactionId, paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
