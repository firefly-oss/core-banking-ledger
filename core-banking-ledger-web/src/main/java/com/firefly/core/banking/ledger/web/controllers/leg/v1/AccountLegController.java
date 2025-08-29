package com.firefly.core.banking.ledger.web.controllers.leg.v1;

import com.firefly.common.core.queries.PaginationRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.core.banking.ledger.core.services.leg.v1.TransactionLegService;
import com.firefly.core.banking.ledger.interfaces.dtos.leg.v1.TransactionLegDTO;
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
 * REST controller for managing account-based transaction leg queries.
 */
@RestController
@RequestMapping("/api/v1/accounts/{accountId}/legs")
@Tag(name = "Account Legs", description = "API endpoints for querying transaction legs by account")
public class AccountLegController {

    @Autowired
    private TransactionLegService service;

    @Operation(
            summary = "List Account Legs",
            description = "Retrieve a paginated list of transaction legs for a specific account."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Account legs retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<TransactionLegDTO>>> listAccountLegs(
            @Parameter(description = "Account ID", required = true)
            @PathVariable Long accountId,

            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size,

            @Parameter(description = "Sort field")
            @RequestParam(required = false) String sort,

            @Parameter(description = "Sort direction (ASC or DESC)")
            @RequestParam(defaultValue = "DESC") String direction
    ) {
        PaginationRequest paginationRequest = new PaginationRequest(page, size, sort, direction);
        return service.listAccountLegs(accountId, paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "List Account Legs by Date Range",
            description = "Retrieve a paginated list of transaction legs for a specific account within a date range."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Account legs retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))
    )
    @GetMapping(value = "/date-range", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<TransactionLegDTO>>> listAccountLegsByDateRange(
            @Parameter(description = "Account ID", required = true)
            @PathVariable Long accountId,

            @Parameter(description = "Start date (ISO format)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,

            @Parameter(description = "End date (ISO format)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,

            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size,

            @Parameter(description = "Sort field")
            @RequestParam(required = false) String sort,

            @Parameter(description = "Sort direction (ASC or DESC)")
            @RequestParam(defaultValue = "DESC") String direction
    ) {
        PaginationRequest paginationRequest = new PaginationRequest(page, size, sort, direction);
        return service.listAccountLegsByDateRange(accountId, startDate, endDate, paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
