package com.firefly.core.banking.ledger.interfaces.dtos.crypto.v1;

import com.firefly.annotations.ValidCurrencyCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.*;

/**
 * Data Transfer Object for crypto deposit transactions.
 * Extends BaseCryptoTransactionDTO with deposit-specific fields.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CryptoDepositDTO extends BaseCryptoTransactionDTO {
    
    /**
     * The recipient's blockchain address (deposit address)
     */
    @NotBlank(message = "Recipient address is required")
    @Size(max = 100, message = "Recipient address cannot exceed 100 characters")
    private String recipientAddress;

    /**
     * The sender's blockchain address (if known)
     */
    @Size(max = 100, message = "Sender address cannot exceed 100 characters")
    private String senderAddress;

    /**
     * Flag indicating whether this is an internal transfer from another account in the system
     */
    private Boolean isInternalTransfer;

    /**
     * The number of confirmations required for the deposit to be considered final
     */
    @Min(value = 0, message = "Required confirmations cannot be negative")
    @Max(value = 100, message = "Required confirmations cannot exceed 100")
    private Integer requiredConfirmations;

    /**
     * Flag indicating whether the deposit should be automatically converted to fiat
     */
    private Boolean autoConvertToFiat;

    /**
     * The fiat currency to convert to if autoConvertToFiat is true
     */
    @ValidCurrencyCode
    private String convertToCurrency;
}