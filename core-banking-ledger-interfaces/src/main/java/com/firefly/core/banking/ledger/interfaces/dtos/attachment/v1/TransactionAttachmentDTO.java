package com.firefly.core.banking.ledger.interfaces.dtos.attachment.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.firefly.core.banking.ledger.interfaces.enums.attachment.v1.AttachmentTypeEnum;
import com.firefly.core.utils.annotations.FilterableId;
import com.firefly.annotations.ValidDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

import java.util.UUID;
/**
 * DTO representing an attachment related to a transaction.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TransactionAttachmentDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID transactionAttachmentId;

    @FilterableId
    @NotNull(message = "Transaction ID is required")
    private UUID transactionId;

    @NotNull(message = "Attachment type is required")
    private AttachmentTypeEnum attachmentType;

    @NotBlank(message = "Attachment name is required")
    @Size(max = 255, message = "Attachment name cannot exceed 255 characters")
    private String attachmentName;

    @Size(max = 500, message = "Attachment description cannot exceed 500 characters")
    private String attachmentDescription;

    @NotBlank(message = "Document ID is required")
    @Size(max = 100, message = "Document ID cannot exceed 100 characters")
    private String documentId;

    @NotBlank(message = "Content type is required")
    @Size(max = 100, message = "Content type cannot exceed 100 characters")
    private String contentType;

    @NotNull(message = "Size in bytes is required")
    @Min(value = 0, message = "Size in bytes cannot be negative")
    private Long sizeBytes;

    @Size(max = 64, message = "SHA256 hash cannot exceed 64 characters")
    private String hashSha256;

    @NotBlank(message = "Uploaded by is required")
    @Size(max = 100, message = "Uploaded by cannot exceed 100 characters")
    private String uploadedBy;

    @NotNull(message = "Upload date is required")
    @ValidDateTime
    private LocalDateTime uploadDate;
}
