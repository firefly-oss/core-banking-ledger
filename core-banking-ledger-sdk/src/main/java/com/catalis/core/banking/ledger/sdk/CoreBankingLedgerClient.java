package com.catalis.core.banking.ledger.sdk;

import com.catalis.core.banking.ledger.sdk.client.*;
import com.catalis.core.banking.ledger.sdk.config.CoreBankingLedgerClientConfig;
import lombok.Getter;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Main client class for the Core Banking Ledger SDK.
 * Serves as the entry point for interacting with the Core Banking Ledger API.
 */
@Getter
public class CoreBankingLedgerClient {

    private final WebClient webClient;
    private final TransactionClient transactionClient;
    private final LedgerAccountClient ledgerAccountClient;
    private final LedgerEntryClient ledgerEntryClient;
    private final TransactionLineCardClient transactionLineCardClient;
    private final TransactionLineDirectDebitClient transactionLineDirectDebitClient;
    private final TransactionLineStandingOrderClient transactionLineStandingOrderClient;
    private final TransactionStatusHistoryClient transactionStatusHistoryClient;
    private final TransactionLineSepaTransferClient transactionLineSepaTransferClient;
    private final TransactionLineWireTransferClient transactionLineWireTransferClient;
    private final TransactionLineDepositClient transactionLineDepositClient;
    private final TransactionLineWithdrawalClient transactionLineWithdrawalClient;
    private final TransactionLineFeeClient transactionLineFeeClient;
    private final TransactionLineInterestClient transactionLineInterestClient;
    private final TransactionLineTransferClient transactionLineTransferClient;
    private final TransactionCategoryClient transactionCategoryClient;

    /**
     * Constructs a new CoreBankingLedgerClient with default configuration.
     */
    public CoreBankingLedgerClient() {
        this(CoreBankingLedgerClientConfig.defaultConfig());
    }

    /**
     * Constructs a new CoreBankingLedgerClient with a custom base URL.
     *
     * @param baseUrl The base URL for the Core Banking Ledger API.
     */
    public CoreBankingLedgerClient(String baseUrl) {
        this(CoreBankingLedgerClientConfig.withBaseUrl(baseUrl));
    }

    /**
     * Constructs a new CoreBankingLedgerClient with a custom configuration.
     *
     * @param config The configuration for the client.
     */
    public CoreBankingLedgerClient(CoreBankingLedgerClientConfig config) {
        this(config.createWebClient());
    }

    /**
     * Constructs a new CoreBankingLedgerClient with a custom WebClient instance.
     * This constructor is primarily used for testing.
     *
     * @param webClient The WebClient instance to use.
     */
    public CoreBankingLedgerClient(WebClient webClient) {
        this.webClient = webClient;
        this.transactionClient = new TransactionClient(webClient);
        this.ledgerAccountClient = new LedgerAccountClient(webClient);
        this.ledgerEntryClient = new LedgerEntryClient(webClient);
        this.transactionLineCardClient = new TransactionLineCardClient(webClient);
        this.transactionLineDirectDebitClient = new TransactionLineDirectDebitClient(webClient);
        this.transactionLineStandingOrderClient = new TransactionLineStandingOrderClient(webClient);
        this.transactionStatusHistoryClient = new TransactionStatusHistoryClient(webClient);
        this.transactionLineSepaTransferClient = new TransactionLineSepaTransferClient(webClient);
        this.transactionLineWireTransferClient = new TransactionLineWireTransferClient(webClient);
        this.transactionLineDepositClient = new TransactionLineDepositClient(webClient);
        this.transactionLineWithdrawalClient = new TransactionLineWithdrawalClient(webClient);
        this.transactionLineFeeClient = new TransactionLineFeeClient(webClient);
        this.transactionLineInterestClient = new TransactionLineInterestClient(webClient);
        this.transactionLineTransferClient = new TransactionLineTransferClient(webClient);
        this.transactionCategoryClient = new TransactionCategoryClient(webClient);
    }
}
