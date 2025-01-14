package com.catalis.core.banking.ledger.web.controllers.ledger.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.core.services.ledger.v1.LedgerEntryServiceImpl;
import com.catalis.core.banking.ledger.interfaces.dtos.ledger.v1.LedgerEntryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Tag(name = "Ledger Entries", description = "APIs for managing ledger entry records in the banking ledger system")
@RestController
@RequestMapping("/api/v1/ledger-entries")
public class LedgerEntryController {

    @Autowired
    private LedgerEntryServiceImpl service;

    @Operation(
            summary = "List Ledger Entries",
            description = "Retrieve a paginated list of all ledger entries, optionally filtered by transaction ID or ledger account ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the ledger entries",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "No ledger entries found",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<LedgerEntryDTO>>> getAllLedgerEntries(
            @Parameter(description = "Filter by transaction ID", required = false)
            @RequestParam(required = false) Long transactionId,

            @Parameter(description = "Filter by ledger account ID", required = false)
            @RequestParam(required = false) Long ledgerAccountId,

            @ParameterObject
            @ModelAttribute PaginationRequest paginationRequest
    ) {
        return service.listLedgerEntries(transactionId, ledgerAccountId, paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create Ledger Entry",
            description = "Create a new ledger entry record."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ledger entry created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LedgerEntryDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ledger entry data provided",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<LedgerEntryDTO>> createLedgerEntry(
            @Parameter(description = "Ledger entry data to be created", required = true,
                    schema = @Schema(implementation = LedgerEntryDTO.class))
            @RequestBody LedgerEntryDTO dto
    ) {
        return service.createLedgerEntry(dto)
                .map(createdEntry -> ResponseEntity.status(201).body(createdEntry))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get Ledger Entry by ID",
            description = "Retrieve a specific ledger entry by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the ledger entry",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LedgerEntryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ledger entry not found",
                    content = @Content)
    })
    @GetMapping(value = "/{ledgerEntryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<LedgerEntryDTO>> getLedgerEntry(
            @Parameter(description = "Unique identifier of the ledger entry", required = true)
            @PathVariable Long ledgerEntryId
    ) {
        return service.getLedgerEntry(ledgerEntryId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Update Ledger Entry",
            description = "Update an existing ledger entry record by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ledger entry updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LedgerEntryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ledger entry not found",
                    content = @Content)
    })
    @PutMapping(value = "/{ledgerEntryId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<LedgerEntryDTO>> updateLedgerEntry(
            @Parameter(description = "Unique identifier of the ledger entry to update", required = true)
            @PathVariable Long ledgerEntryId,

            @Parameter(description = "Updated ledger entry data", required = true,
                    schema = @Schema(implementation = LedgerEntryDTO.class))
            @RequestBody LedgerEntryDTO dto
    ) {
        return service.updateLedgerEntry(ledgerEntryId, dto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete Ledger Entry",
            description = "Remove an existing ledger entry record by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ledger entry deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Ledger entry not found",
                    content = @Content)
    })
    @DeleteMapping(value = "/{ledgerEntryId}")
    public Mono<ResponseEntity<Void>> deleteLedgerEntry(
            @Parameter(description = "Unique identifier of the ledger entry to delete", required = true)
            @PathVariable Long ledgerEntryId
    ) {
        return service.deleteLedgerEntry(ledgerEntryId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}