package com.catalis.core.banking.ledger.web.controllers.ledger.v1;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.core.services.ledger.v1.LedgerAccountServiceImpl;
import com.catalis.core.banking.ledger.interfaces.dtos.ledger.v1.LedgerAccountDTO;
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

@Tag(name = "Ledger Accounts", description = "APIs for managing ledger account records in the banking ledger system")
@RestController
@RequestMapping("/api/v1/ledger-accounts")
public class LedgerAccountController {

    @Autowired
    private LedgerAccountServiceImpl service;

    @Operation(
            summary = "List Ledger Accounts",
            description = "Retrieve a paginated list of all ledger account records."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the ledger accounts",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "No ledger accounts found",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<LedgerAccountDTO>>> getAllLedgerAccounts(
            @ParameterObject
            @ModelAttribute PaginationRequest paginationRequest
    ) {
        return service.listLedgerAccounts(paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Filter Ledger Accounts",
            description = "Apply custom filters to retrieve a paginated list of ledger accounts."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved filtered ledger accounts",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "No filtered results found",
                    content = @Content)
    })
    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<LedgerAccountDTO>>> filterProducts(
            @ParameterObject
            @ModelAttribute FilterRequest<LedgerAccountDTO> filterRequest
    ) {
        return service.filterLedgerAccounts(filterRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create Ledger Account",
            description = "Create a new ledger account record in the ledger system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ledger account created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LedgerAccountDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ledger account data provided",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<LedgerAccountDTO>> createLedgerAccount(
            @Parameter(description = "Ledger account data to be created", required = true,
                    schema = @Schema(implementation = LedgerAccountDTO.class))
            @RequestBody LedgerAccountDTO dto
    ) {
        return service.createLedgerAccount(dto)
                .map(createdAccount -> ResponseEntity.status(201).body(createdAccount))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get Ledger Account by ID",
            description = "Retrieve a specific ledger account by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the ledger account",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LedgerAccountDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ledger account not found",
                    content = @Content)
    })
    @GetMapping(value = "/{ledgerAccountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<LedgerAccountDTO>> getLedgerAccount(
            @Parameter(description = "Unique identifier of the ledger account to retrieve", required = true)
            @PathVariable Long ledgerAccountId
    ) {
        return service.getLedgerAccount(ledgerAccountId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Update Ledger Account",
            description = "Update an existing ledger account record by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ledger account updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LedgerAccountDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ledger account not found",
                    content = @Content)
    })
    @PutMapping(value = "/{ledgerAccountId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<LedgerAccountDTO>> updateLedgerAccount(
            @Parameter(description = "Unique identifier of the ledger account to update", required = true)
            @PathVariable Long ledgerAccountId,

            @Parameter(description = "Updated ledger account data", required = true,
                    schema = @Schema(implementation = LedgerAccountDTO.class))
            @RequestBody LedgerAccountDTO dto
    ) {
        return service.updateLedgerAccount(ledgerAccountId, dto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete Ledger Account",
            description = "Remove an existing ledger account record by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ledger account deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Ledger account not found",
                    content = @Content)
    })
    @DeleteMapping(value = "/{ledgerAccountId}")
    public Mono<ResponseEntity<Void>> deleteLedgerAccount(
            @Parameter(description = "Unique identifier of the ledger account to delete", required = true)
            @PathVariable Long ledgerAccountId
    ) {
        return service.deleteLedgerAccount(ledgerAccountId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}