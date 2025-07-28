package com.catalis.core.banking.ledger.interfaces.dtos.crypto.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
    private String recipientAddress;
    
    /**
     * The sender's blockchain address (if known)
     */
    private String senderAddress;
    
    /**
     * Flag indicating whether this is an internal transfer from another account in the system
     */
    private Boolean isInternalTransfer;
    
    /**
     * The number of confirmations required for the deposit to be considered final
     */
    private Integer requiredConfirmations;
    
    /**
     * Flag indicating whether the deposit should be automatically converted to fiat
     */
    private Boolean autoConvertToFiat;
    
    /**
     * The fiat currency to convert to if autoConvertToFiat is true
     */
    private String convertToCurrency;
}