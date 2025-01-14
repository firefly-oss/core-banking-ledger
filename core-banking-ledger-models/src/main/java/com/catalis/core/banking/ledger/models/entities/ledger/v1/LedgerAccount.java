package com.catalis.core.banking.ledger.models.entities.ledger.v1;

import com.catalis.core.banking.ledger.interfaces.enums.ledger.v1.LedgerAccountTypeEnum;
import com.catalis.core.banking.ledger.models.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("ledger_account")
public class LedgerAccount extends BaseEntity {

    @Id
    @Column("ledger_account_id")
    private Long ledgerAccountId;

    @Column("account_code")
    private String accountCode;

    @Column("account_name")
    private String accountName;

    @Column("account_type")
    private LedgerAccountTypeEnum accountType;

    @Column("parent_account_id")
    private Long parentAccountId;

    @Column("is_active")
    private Boolean isActive;
}
