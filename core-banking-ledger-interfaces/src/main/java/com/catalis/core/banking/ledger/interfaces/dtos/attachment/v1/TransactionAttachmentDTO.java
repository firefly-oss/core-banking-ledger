package com.catalis.core.banking.ledger.interfaces.dtos.attachment.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.catalis.core.utils.annotations.FilterableId;
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

    private String attachmentType;
    private String attachmentName;
    private String attachmentDescription;
    private String objectStorageUrl;
    private String contentType;
    private Long sizeBytes;
    private String hashSha256;
    private String uploadedBy;
    private LocalDateTime uploadDate;
}
