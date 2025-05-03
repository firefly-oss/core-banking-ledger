package com.catalis.core.banking.ledger.sdk;

import com.catalis.core.banking.ledger.sdk.client.*;
import com.catalis.core.banking.ledger.sdk.config.CoreBankingLedgerClientConfig;
import lombok.Getter;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Main client class for the Core Banking Ledger SDK.
 * <p>
 * This class serves as the entry point for the SDK and provides access to all specific client instances
 * for interacting with different parts of the Core Banking Ledger API.
 * <p>
 * Example usage:
 * <pre>
 * // Create a client with default configuration
 * CoreBankingLedgerClient client = new CoreBankingLedgerClient();
 *
 * // Or with a custom base URL
 * CoreBankingLedgerClient client = new CoreBankingLedgerClient("https://api.example.com");
 *
 * // Or with a fully customized configuration
 * CoreBankingLedgerClientConfig config = CoreBankingLedgerClientConfig.builder()
 *     .baseUrl("https://api.example.com")
 *     .connectTimeoutMs(10000)
 *     .readTimeoutMs(10000)
 *     .writeTimeoutMs(10000)
 *     .enableLogging(true)
 *     .build();
 * CoreBankingLedgerClient client = new CoreBankingLedgerClient(config);
 *
 * // Use the client to interact with the API
 * Mono&lt;TransactionDTO&gt; transaction = client.getTransactionClient().getTransaction(123L);
 * </pre>
 */
public class CoreBankingLedgerClient {

    /**
     * The WebClient instance used for making HTTP requests.
     */
    @Getter
    private final WebClient webClient;

    /**
     * The configuration used to create this client.
     */
    @Getter
    private final CoreBankingLedgerClientConfig config;

    /**
     * The transaction client for interacting with transaction API endpoints.
     */
    @Getter
    private final TransactionClient transactionClient;

    /**
     * The account leg client for interacting with account leg API endpoints.
     */
    @Getter
    private final AccountLegClient accountLegClient;

    /**
     * The transaction leg client for interacting with transaction leg API endpoints.
     */
    @Getter
    private final TransactionLegClient transactionLegClient;

    /**
     * The transaction attachment client for interacting with transaction attachment API endpoints.
     */
    @Getter
    private final TransactionAttachmentClient transactionAttachmentClient;

    /**
     * The transaction category client for interacting with transaction category API endpoints.
     */
    @Getter
    private final TransactionCategoryClient transactionCategoryClient;

    /**
     * The transaction status history client for interacting with transaction status history API endpoints.
     */
    @Getter
    private final TransactionStatusHistoryClient transactionStatusHistoryClient;

    /**
     * The transaction line interest client for interacting with transaction line interest API endpoints.
     */
    @Getter
    private final TransactionLineInterestClient transactionLineInterestClient;

    /**
     * The transaction line card client for interacting with transaction line card API endpoints.
     */
    @Getter
    private final TransactionLineCardClient transactionLineCardClient;

    /**
     * The transaction line deposit client for interacting with transaction line deposit API endpoints.
     */
    @Getter
    private final TransactionLineDepositClient transactionLineDepositClient;

    /**
     * The transaction line direct debit client for interacting with transaction line direct debit API endpoints.
     */
    @Getter
    private final TransactionLineDirectDebitClient transactionLineDirectDebitClient;

    /**
     * Constructs a new CoreBankingLedgerClient with default configuration.
     */
    public CoreBankingLedgerClient() {
        this(CoreBankingLedgerClientConfig.builder().build());
    }

    /**
     * Constructs a new CoreBankingLedgerClient with a custom base URL.
     *
     * @param baseUrl The base URL for the Core Banking Ledger API.
     */
    public CoreBankingLedgerClient(String baseUrl) {
        this(CoreBankingLedgerClientConfig.builder().baseUrl(baseUrl).build());
    }

    /**
     * Constructs a new CoreBankingLedgerClient with a custom configuration.
     *
     * @param config The configuration for the client.
     */
    public CoreBankingLedgerClient(CoreBankingLedgerClientConfig config) {
        this.config = config;
        this.webClient = config.createWebClient();
        this.transactionClient = new TransactionClient(webClient);
        this.accountLegClient = new AccountLegClient(webClient);
        this.transactionLegClient = new TransactionLegClient(webClient);
        this.transactionAttachmentClient = new TransactionAttachmentClient(webClient);
        this.transactionCategoryClient = new TransactionCategoryClient(webClient);
        this.transactionStatusHistoryClient = new TransactionStatusHistoryClient(webClient);
        this.transactionLineInterestClient = new TransactionLineInterestClient(webClient);
        this.transactionLineCardClient = new TransactionLineCardClient(webClient);
        this.transactionLineDepositClient = new TransactionLineDepositClient(webClient);
        this.transactionLineDirectDebitClient = new TransactionLineDirectDebitClient(webClient);
    }

    /**
     * Constructs a new CoreBankingLedgerClient with a custom WebClient instance.
     * This constructor is primarily intended for testing.
     *
     * @param webClient The WebClient instance to use for making HTTP requests.
     */
    public CoreBankingLedgerClient(WebClient webClient) {
        this.config = null;
        this.webClient = webClient;
        this.transactionClient = new TransactionClient(webClient);
        this.accountLegClient = new AccountLegClient(webClient);
        this.transactionLegClient = new TransactionLegClient(webClient);
        this.transactionAttachmentClient = new TransactionAttachmentClient(webClient);
        this.transactionCategoryClient = new TransactionCategoryClient(webClient);
        this.transactionStatusHistoryClient = new TransactionStatusHistoryClient(webClient);
        this.transactionLineInterestClient = new TransactionLineInterestClient(webClient);
        this.transactionLineCardClient = new TransactionLineCardClient(webClient);
        this.transactionLineDepositClient = new TransactionLineDepositClient(webClient);
        this.transactionLineDirectDebitClient = new TransactionLineDirectDebitClient(webClient);
    }
}
