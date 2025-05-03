package com.catalis.core.banking.ledger.models.entities.attachment.v1;

import com.catalis.core.banking.ledger.models.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entity representing an attachment related to a transaction.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("transaction_attachment")
public class TransactionAttachment extends BaseEntity {
    @Id
    @Column("transaction_attachment_id")
    private Long transactionAttachmentId;

    @Column("transaction_id")
    private Long transactionId;

    @Column("attachment_type")
    private String attachmentType;

    @Column("attachment_name")
    private String attachmentName;

    @Column("attachment_description")
    private String attachmentDescription;

    @Column("object_storage_url")
    private String objectStorageUrl;

    @Column("content_type")
    private String contentType;

    @Column("size_bytes")
    private Long sizeBytes;

    @Column("hash_sha256")
    private String hashSha256;

    @Column("uploaded_by")
    private String uploadedBy;

    @Column("upload_date")
    private LocalDateTime uploadDate;
}
