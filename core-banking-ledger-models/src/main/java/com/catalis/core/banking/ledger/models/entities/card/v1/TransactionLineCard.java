package com.catalis.core.banking.ledger.models.entities.card.v1;

import com.catalis.core.banking.ledger.models.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("transaction_line_card")
public class TransactionLineCard extends BaseEntity {
    @Id
    @Column("transaction_line_card_id")
    private Long transactionLineCardId;

    @Column("transaction_id")
    private Long transactionId;

    @Column("card_auth_code")
    private String cardAuthCode;

    @Column("card_merchant_category_code")
    private String cardMerchantCategoryCode;

    @Column("card_merchant_name")
    private String cardMerchantName;

    @Column("card_pos_entry_mode")
    private String cardPosEntryMode;

    @Column("card_transaction_reference")
    private String cardTransactionReference;

    @Column("card_terminal_id")
    private String cardTerminalId;

    @Column("card_holder_country")
    private String cardHolderCountry;

    @Column("card_present_flag")
    private Boolean cardPresentFlag;

    @Column("card_transaction_timestamp")
    private LocalDateTime cardTransactionTimestamp;

    @Column("card_fraud_flag")
    private Boolean cardFraudFlag;

    @Column("card_currency_conversion_rate")
    private BigDecimal cardCurrencyConversionRate;

    @Column("card_fee_amount")
    private BigDecimal cardFeeAmount;

    @Column("card_fee_currency")
    private String cardFeeCurrency;

    @Column("card_installment_plan")
    private String cardInstallmentPlan;
}
