package com.firefly.core.banking.ledger.models.entities.statement.v1;

import com.firefly.core.banking.ledger.interfaces.enums.statement.v1.StatementPeriodEnum;
import com.firefly.core.banking.ledger.models.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing a statement record.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("statement")
public class Statement extends BaseEntity {
    @Id
    @Column("statement_id")
    private Long statementId;

    @Column("account_id")
    private Long accountId;

    @Column("account_space_id")
    private Long accountSpaceId;

    @Column("period_type")
    private StatementPeriodEnum periodType;

    @Column("start_date")
    private LocalDate startDate;

    @Column("end_date")
    private LocalDate endDate;

    @Column("generation_date")
    private LocalDateTime generationDate;

    @Column("transaction_count")
    private Integer transactionCount;

    @Column("included_pending")
    private Boolean includedPending;

    @Column("included_details")
    private Boolean includedDetails;
}
