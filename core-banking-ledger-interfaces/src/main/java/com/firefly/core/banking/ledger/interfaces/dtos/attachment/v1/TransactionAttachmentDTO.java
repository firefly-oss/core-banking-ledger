package com.firefly.core.banking.ledger.interfaces.dtos.attachment.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.firefly.core.banking.ledger.interfaces.enums.attachment.v1.AttachmentTypeEnum;
import com.firefly.core.utils.annotations.FilterableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * DTO representing an attachment related to a transaction.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TransactionAttachmentDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long transactionAttachmentId;

    @FilterableId
    private Long transactionId;

    private AttachmentTypeEnum attachmentType;
    private String attachmentName;
    private String attachmentDescription;
    private String documentId;
    private String contentType;
    private Long sizeBytes;
    private String hashSha256;
    private String uploadedBy;
    private LocalDateTime uploadDate;
}
