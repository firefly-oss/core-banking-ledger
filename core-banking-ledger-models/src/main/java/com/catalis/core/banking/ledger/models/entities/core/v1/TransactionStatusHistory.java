package com.catalis.core.banking.ledger.models.entities.core.v1;

import com.catalis.core.banking.ledger.interfaces.enums.core.v1.TransactionStatusEnum;
import com.catalis.core.banking.ledger.models.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("transaction_status_history")
public class TransactionStatusHistory extends BaseEntity {
    @Id
    @Column("transaction_status_history_id")
    private Long transactionStatusHistoryId;

    @Column("transaction_id")
    private Long transactionId;

    @Column("status_code")
    private TransactionStatusEnum statusCode;

    @Column("status_start_datetime")
    private LocalDateTime statusStartDatetime;

    @Column("status_end_datetime")
    private LocalDateTime statusEndDatetime;

    @Column("reason")
    private String reason;

    @Column("regulated_reporting_flag")
    private Boolean regulatedReportingFlag;
}
