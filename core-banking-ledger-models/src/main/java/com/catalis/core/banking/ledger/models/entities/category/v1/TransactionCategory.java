package com.catalis.core.banking.ledger.models.entities.category.v1;

import com.catalis.core.banking.ledger.interfaces.enums.category.v1.CategoryTypeEnum;
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
@Table("transaction_category")
public class TransactionCategory extends BaseEntity {
    @Id
    @Column("transaction_category_id")
    private Long transactionCategoryId;

    @Column("parent_category_id")
    private Long parentCategoryId;

    @Column("category_name")
    private String categoryName;

    @Column("category_description")
    private String categoryDescription;

    @Column("category_type")
    private CategoryTypeEnum categoryType;

    @Column("spanish_tax_code")
    private String spanishTaxCode;
}

