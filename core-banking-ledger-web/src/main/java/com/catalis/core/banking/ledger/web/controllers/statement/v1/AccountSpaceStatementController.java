package com.catalis.core.banking.ledger.web.controllers.statement.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.core.services.statement.v1.StatementService;
import com.catalis.core.banking.ledger.interfaces.dtos.statement.v1.StatementDTO;
import com.catalis.core.banking.ledger.interfaces.dtos.statement.v1.StatementMetadataDTO;
import com.catalis.core.banking.ledger.interfaces.dtos.statement.v1.StatementRequestDTO;
import com.catalis.core.banking.ledger.interfaces.enums.statement.v1.StatementFormatEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

/**
 * REST controller for managing account space statements.
 */
@RestController
@RequestMapping("/api/v1/account-spaces/{accountSpaceId}/statements")
@Tag(name = "Account Space Statements", description = "API endpoints for managing account space statements")
public class AccountSpaceStatementController {

    @Autowired
    private StatementService service;

    @Operation(
            summary = "Generate Account Space Statement",
            description = "Generate a new statement for a specific account space based on the provided parameters."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Statement generated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StatementDTO.class))
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<StatementDTO>> generateStatement(
            @Parameter(description = "Account Space ID", required = true)
            @PathVariable Long accountSpaceId,

            @Parameter(description = "Statement request parameters", required = true,
                    schema = @Schema(implementation = StatementRequestDTO.class))
            @RequestBody StatementRequestDTO requestDTO
    ) {
        return service.generateAccountSpaceStatement(accountSpaceId, requestDTO)
                .map(statement -> ResponseEntity.ok().body(statement))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "List Account Space Statements",
            description = "Retrieve a paginated list of statements for a specific account space."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Account space statements retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<StatementMetadataDTO>>> listStatements(
            @Parameter(description = "Account Space ID", required = true)
            @PathVariable Long accountSpaceId,

            @ParameterObject
            @ModelAttribute PaginationRequest paginationRequest
    ) {
        return service.listAccountSpaceStatements(accountSpaceId, paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "List Account Space Statements by Date Range",
            description = "Retrieve a paginated list of statements for a specific account space within a date range."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Account space statements retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginationResponse.class))
    )
    @GetMapping(value = "/date-range", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<StatementMetadataDTO>>> listStatementsByDateRange(
            @Parameter(description = "Account Space ID", required = true)
            @PathVariable Long accountSpaceId,

            @Parameter(description = "Start date (ISO format)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @Parameter(description = "End date (ISO format)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            @ParameterObject
            @ModelAttribute PaginationRequest paginationRequest
    ) {
        return service.listAccountSpaceStatementsByDateRange(accountSpaceId, startDate, endDate, paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Get Account Space Statement",
            description = "Retrieve a specific statement by ID."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Statement retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StatementMetadataDTO.class))
    )
    @GetMapping(value = "/{statementId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<StatementMetadataDTO>> getStatement(
            @Parameter(description = "Account Space ID", required = true)
            @PathVariable Long accountSpaceId,

            @Parameter(description = "Statement ID", required = true)
            @PathVariable Long statementId
    ) {
        return service.getStatement(statementId)
                .filter(statement -> statement.getAccountSpaceId() != null && statement.getAccountSpaceId().equals(accountSpaceId))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Download Account Space Statement",
            description = "Download a specific statement file."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Statement file downloaded successfully",
            content = @Content(mediaType = "application/octet-stream")
    )
    @GetMapping(value = "/{statementId}/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Mono<ResponseEntity<byte[]>> downloadStatement(
            @Parameter(description = "Account Space ID", required = true)
            @PathVariable Long accountSpaceId,

            @Parameter(description = "Statement ID", required = true)
            @PathVariable Long statementId
    ) {
        return service.getStatement(statementId)
                .filter(statement -> statement.getAccountSpaceId() != null && statement.getAccountSpaceId().equals(accountSpaceId))
                .flatMap(statement -> service.downloadStatement(statementId)
                        .map(fileContent -> {
                            HttpHeaders headers = new HttpHeaders();
                            headers.setContentDispositionFormData("attachment", getFileName(statement));
                            headers.setContentType(getMediaType(statement.getFormat()));
                            return ResponseEntity.ok()
                                    .headers(headers)
                                    .body(fileContent);
                        }))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Get the file name for a statement.
     *
     * @param statement The statement metadata.
     * @return The file name.
     */
    private String getFileName(StatementMetadataDTO statement) {
        return String.format("account_space_%d_statement_%s_to_%s.%s",
                statement.getAccountSpaceId(),
                statement.getStartDate().toString(),
                statement.getEndDate().toString(),
                statement.getFormat().toString().toLowerCase());
    }

    /**
     * Get the media type for a statement format.
     *
     * @param format The statement format.
     * @return The media type.
     */
    private MediaType getMediaType(StatementFormatEnum format) {
        switch (format) {
            case PDF:
                return MediaType.APPLICATION_PDF;
            case CSV:
                return MediaType.parseMediaType("text/csv");
            case JSON:
                return MediaType.APPLICATION_JSON;
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
