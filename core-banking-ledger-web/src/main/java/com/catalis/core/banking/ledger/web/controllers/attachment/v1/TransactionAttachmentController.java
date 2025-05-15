package com.catalis.core.banking.ledger.web.controllers.attachment.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.core.services.attachment.v1.TransactionAttachmentService;
import com.catalis.core.banking.ledger.interfaces.dtos.attachment.v1.TransactionAttachmentDTO;
import com.catalis.core.banking.ledger.interfaces.enums.attachment.v1.AttachmentTypeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * REST controller for managing transaction attachments.
 */
@RestController
@RequestMapping("/api/v1/transactions/{transactionId}/attachments")
@Tag(name = "Transaction Attachments", description = "API endpoints for managing transaction attachments")
public class TransactionAttachmentController {

    @Autowired
    private TransactionAttachmentService service;

    @Operation(
            summary = "Create Transaction Attachment",
            description = "Create a new attachment for a specific transaction."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Attachment created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionAttachmentDTO.class))
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TransactionAttachmentDTO>> createAttachment(
            @Parameter(description = "Transaction ID", required = true)
            @PathVariable Long transactionId,

            @Parameter(description = "Attachment data", required = true,
                    schema = @Schema(implementation = TransactionAttachmentDTO.class))
            @RequestBody TransactionAttachmentDTO attachmentDTO
    ) {
        return service.createAttachment(transactionId, attachmentDTO)
                .map(createdAttachment -> ResponseEntity.status(201).body(createdAttachment))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get Transaction Attachment",
            description = "Retrieve a specific attachment by its ID."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Attachment retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionAttachmentDTO.class))
    )
    @GetMapping(value = "/{attachmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TransactionAttachmentDTO>> getAttachment(
            @Parameter(description = "Transaction ID", required = true)
            @PathVariable Long transactionId,

            @Parameter(description = "Attachment ID", required = true)
            @PathVariable Long attachmentId
    ) {
        return service.getAttachment(transactionId, attachmentId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "List Transaction Attachments",
            description = "Retrieve a paginated list of attachments for a specific transaction."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Attachments retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<TransactionAttachmentDTO>>> listAttachments(
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
        PaginationRequest paginationRequest = new PaginationRequest(page, size, sort, direction);
        return service.listAttachments(transactionId, paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "List Transaction Attachments by Type",
            description = "Retrieve a paginated list of attachments of a specific type for a transaction."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Attachments retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))
    )
    @GetMapping(value = "/type/{attachmentType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<TransactionAttachmentDTO>>> listAttachmentsByType(
            @Parameter(description = "Transaction ID", required = true)
            @PathVariable Long transactionId,

            @Parameter(description = "Attachment type", required = true, schema = @Schema(implementation = AttachmentTypeEnum.class))
            @PathVariable AttachmentTypeEnum attachmentType,

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
        return service.listAttachmentsByType(transactionId, attachmentType, paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
