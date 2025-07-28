package com.catalis.core.banking.ledger.interfaces.enums.core.v1;

public enum TransactionTypeEnum {
    DEPOSIT,
    WITHDRAWAL,
    TRANSFER,
    FEE,
    INTEREST,
    WIRE_TRANSFER,
    STANDING_ORDER,
    ACH,
    CARD,
    DIRECT_DEBIT,
    SEPA_TRANSFER,
    
    // Crypto-specific transaction types
    CRYPTO_DEPOSIT,
    CRYPTO_WITHDRAWAL,
    CRYPTO_TRANSFER,
    CRYPTO_SWAP,
    TOKEN_MINT,
    TOKEN_BURN,
    NFT_TRANSFER,
    STAKING,
    UNSTAKING,
    REWARD
}
