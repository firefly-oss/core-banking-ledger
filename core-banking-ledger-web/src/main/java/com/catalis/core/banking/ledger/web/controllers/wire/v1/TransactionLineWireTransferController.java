package com.catalis.core.banking.ledger.web.controllers.wire.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.common.web.error.models.ErrorResponse;
import com.catalis.core.banking.ledger.core.services.wire.v1.TransactionLineWireTransferCreateService;
import com.catalis.core.banking.ledger.core.services.wire.v1.TransactionLineWireTransferDeleteService;
import com.catalis.core.banking.ledger.core.services.wire.v1.TransactionLineWireTransferGetService;
import com.catalis.core.banking.ledger.core.services.wire.v1.TransactionLineWireTransferUpdateService;
import com.catalis.core.banking.ledger.interfaces.dtos.wire.v1.TransactionLineWireTransferDTO;
import com.catalis.core.banking.ledger.interfaces.dtos.wire.v1.TransactionSearchRequest;
import com.catalis.core.banking.ledger.interfaces.dtos.wire.v1.TransactionWireFilterDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/wire-transfers")
@Tag(name = "Wire Transfers", description = "Operations for managing wire transfers")
public class TransactionLineWireTransferController {

    @Autowired
    private TransactionLineWireTransferCreateService createService;

    @Autowired
    private TransactionLineWireTransferUpdateService updateService;

    @Autowired
    private TransactionLineWireTransferDeleteService deleteService;

    @Autowired
    private TransactionLineWireTransferUpdateService updateStatusService;

    @Autowired
    private TransactionLineWireTransferGetService getService;


    @Operation(summary = "Create wire transfer", description = "Creates a new wire transfer transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Wire transfer created successfully",
                    content = @Content(schema = @Schema(implementation = TransactionLineWireTransferDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input provided",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public Mono<ResponseEntity<TransactionLineWireTransferDTO>> createWireTransfer(
            @RequestBody @Valid TransactionLineWireTransferDTO wireTransferDTO) {
        return createService.createTransactionLineWireTransfer(wireTransferDTO)
                .map(created -> ResponseEntity.status(201).body(created));
    }

    @Operation(summary = "Get wire transfer details", description = "Retrieves details of a specific wire transfer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wire transfer found",
                    content = @Content(schema = @Schema(implementation = TransactionLineWireTransferDTO.class))),
            @ApiResponse(responseCode = "404", description = "Wire transfer not found")
    })
    @GetMapping("/details/{wireTransferId}")
    public Mono<ResponseEntity<TransactionLineWireTransferDTO>> getWireTransfer(
            @Parameter(description = "ID of the wire transfer", required = true)
            @PathVariable Long wireTransferId) {
        return getService.getWireTransfer(wireTransferId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update wire transfer", description = "Updates an existing wire transfer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wire transfer updated successfully",
                    content = @Content(schema = @Schema(implementation = TransactionLineWireTransferDTO.class))),
            @ApiResponse(responseCode = "404", description = "Wire transfer not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{wireTransferId}")
    public Mono<ResponseEntity<TransactionLineWireTransferDTO>> updateWireTransfer(
            @Parameter(description = "ID of the wire transfer to update", required = true)
            @PathVariable Long wireTransferId,
            @RequestBody @Valid TransactionLineWireTransferDTO wireTransferDTO) {
        return updateService.updateTransactionLineWireTransfer(wireTransferId, wireTransferDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete wire transfer", description = "Deletes a wire transfer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Wire transfer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Wire transfer not found")
    })
    @DeleteMapping("/{wireTransferId}")
    public Mono<ResponseEntity<Void>> deleteWireTransfer(
            @Parameter(description = "ID of the wire transfer to delete", required = true)
            @PathVariable Long wireTransferId) {
        return deleteService.deleteTransactionLineWireTransfer(wireTransferId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Search wire transfers", description = "Search wire transfers based on various criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully",
                    content = @Content(schema = @Schema(implementation = PaginationResponse.class)))
    })
    @PostMapping("/search")
    public Mono<PaginationResponse<TransactionLineWireTransferDTO>> searchWireTransfers(
            @Parameter(description = "Search criteria and pagination options", required = true)
            @RequestBody @Valid TransactionSearchRequest searchRequest) {

        TransactionWireFilterDTO filter = searchRequest.getTransactionFilter();
        PaginationRequest pagination = searchRequest.getPagination();

        return getService.searchWireTransfers(filter, pagination);
    }

}