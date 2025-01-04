package com.catalis.core.banking.ledger.web.controllers.standingorder.v1;


import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.common.web.error.models.ErrorResponse;
import com.catalis.core.banking.ledger.core.services.standingorder.v1.TransactionLineStandingOrderCreateService;
import com.catalis.core.banking.ledger.core.services.standingorder.v1.TransactionLineStandingOrderDeleteService;
import com.catalis.core.banking.ledger.core.services.standingorder.v1.TransactionLineStandingOrderGetService;
import com.catalis.core.banking.ledger.core.services.standingorder.v1.TransactionLineStandingOrderUpdateService;
import com.catalis.core.banking.ledger.interfaces.dtos.standingorder.v1.TransactionLineStandingOrderDTO;
import com.catalis.core.banking.ledger.interfaces.dtos.standingorder.v1.TransactionSearchRequest;
import com.catalis.core.banking.ledger.interfaces.dtos.standingorder.v1.TransactionStandingOrderFilterDTO;
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
@RequestMapping("/api/v1/standing-orders")
@Tag(name = "Standing Orders", description = "Operations for managing standing orders")
public class TransactionLineStandingOrderController {

    @Autowired
    private TransactionLineStandingOrderCreateService createService;

    @Autowired
    private TransactionLineStandingOrderUpdateService updateService;

    @Autowired
    private TransactionLineStandingOrderDeleteService deleteService;

    @Autowired
    private TransactionLineStandingOrderGetService getService;

    @Operation(summary = "Create standing order", description = "Creates a new standing order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Standing order created successfully",
                    content = @Content(schema = @Schema(implementation = TransactionLineStandingOrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input provided",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public Mono<ResponseEntity<TransactionLineStandingOrderDTO>> createStandingOrder(
            @RequestBody @Valid TransactionLineStandingOrderDTO standingOrderDTO) {
        return createService.createTransactionLineStandingOrder(standingOrderDTO)
                .map(created -> ResponseEntity.status(201).body(created));
    }

    @Operation(summary = "Get standing order details", description = "Retrieves details of a specific standing order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Standing order found",
                    content = @Content(schema = @Schema(implementation = TransactionLineStandingOrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Standing order not found")
    })
    @GetMapping("/details/{standingOrderId}")
    public Mono<ResponseEntity<TransactionLineStandingOrderDTO>> getStandingOrder(
            @Parameter(description = "ID of the standing order", required = true)
            @PathVariable Long standingOrderId) {
        return getService.getStandingOrder(standingOrderId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update standing order", description = "Updates an existing standing order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Standing order updated successfully",
                    content = @Content(schema = @Schema(implementation = TransactionLineStandingOrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Standing order not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{standingOrderId}")
    public Mono<ResponseEntity<TransactionLineStandingOrderDTO>> updateStandingOrder(
            @Parameter(description = "ID of the standing order to update", required = true)
            @PathVariable Long standingOrderId,
            @RequestBody @Valid TransactionLineStandingOrderDTO standingOrderDTO) {
        return updateService.updateTransactionLineStandingOrder(standingOrderId, standingOrderDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete standing order", description = "Deletes a standing order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Standing order deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Standing order not found")
    })
    @DeleteMapping("/{standingOrderId}")
    public Mono<ResponseEntity<Void>> deleteStandingOrder(
            @Parameter(description = "ID of the standing order to delete", required = true)
            @PathVariable Long standingOrderId) {
        return deleteService.deleteTransactionLineStandingOrder(standingOrderId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Search standing orders", description = "Search standing orders based on various criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully",
                    content = @Content(schema = @Schema(implementation = PaginationResponse.class)))
    })
    @PostMapping("/search")
    public Mono<PaginationResponse<TransactionLineStandingOrderDTO>> searchStandingOrders(
            @Parameter(description = "Search criteria and pagination options", required = true)
            @RequestBody @Valid TransactionSearchRequest searchRequest) {

        TransactionStandingOrderFilterDTO filter = searchRequest.getTransactionFilter();
        PaginationRequest pagination = searchRequest.getPagination();

        return getService.searchStandingOrders(filter, pagination);
    }


}