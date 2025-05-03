package com.catalis.core.banking.ledger.sdk;

import com.catalis.core.banking.ledger.sdk.config.CoreBankingLedgerClientConfig;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link CoreBankingLedgerClient}.
 */
class CoreBankingLedgerClientTest {

    private static final String CUSTOM_BASE_URL = "https://api.example.com";

    /**
     * Test initialization with default configuration.
     */
    @Test
    void testDefaultConstructor() {
        CoreBankingLedgerClient client = new CoreBankingLedgerClient();

        assertNotNull(client.getWebClient(), "WebClient should not be null");
        assertNotNull(client.getConfig(), "Config should not be null");
        assertNotNull(client.getTransactionClient(), "TransactionClient should not be null");
        assertNotNull(client.getAccountLegClient(), "AccountLegClient should not be null");
        assertNotNull(client.getTransactionLegClient(), "TransactionLegClient should not be null");
        assertNotNull(client.getTransactionAttachmentClient(), "TransactionAttachmentClient should not be null");
        assertNotNull(client.getTransactionCategoryClient(), "TransactionCategoryClient should not be null");
        assertNotNull(client.getTransactionStatusHistoryClient(), "TransactionStatusHistoryClient should not be null");
        assertNotNull(client.getTransactionLineInterestClient(), "TransactionLineInterestClient should not be null");
        assertNotNull(client.getTransactionLineCardClient(), "TransactionLineCardClient should not be null");
        assertNotNull(client.getTransactionLineDepositClient(), "TransactionLineDepositClient should not be null");
        assertNotNull(client.getTransactionLineDirectDebitClient(), "TransactionLineDirectDebitClient should not be null");

        assertEquals(CoreBankingLedgerClientConfig.DEFAULT_BASE_URL, client.getConfig().getBaseUrl(),
                "Default base URL should be used");
        assertEquals(CoreBankingLedgerClientConfig.DEFAULT_CONNECT_TIMEOUT_MS, client.getConfig().getConnectTimeoutMs(),
                "Default connect timeout should be used");
        assertEquals(CoreBankingLedgerClientConfig.DEFAULT_READ_TIMEOUT_MS, client.getConfig().getReadTimeoutMs(),
                "Default read timeout should be used");
        assertEquals(CoreBankingLedgerClientConfig.DEFAULT_WRITE_TIMEOUT_MS, client.getConfig().getWriteTimeoutMs(),
                "Default write timeout should be used");
        assertFalse(client.getConfig().isEnableLogging(), "Logging should be disabled by default");
    }

    /**
     * Test initialization with custom base URL.
     */
    @Test
    void testBaseUrlConstructor() {
        CoreBankingLedgerClient client = new CoreBankingLedgerClient(CUSTOM_BASE_URL);

        assertNotNull(client.getWebClient(), "WebClient should not be null");
        assertNotNull(client.getConfig(), "Config should not be null");
        assertNotNull(client.getTransactionClient(), "TransactionClient should not be null");
        assertNotNull(client.getAccountLegClient(), "AccountLegClient should not be null");
        assertNotNull(client.getTransactionLegClient(), "TransactionLegClient should not be null");
        assertNotNull(client.getTransactionAttachmentClient(), "TransactionAttachmentClient should not be null");
        assertNotNull(client.getTransactionCategoryClient(), "TransactionCategoryClient should not be null");
        assertNotNull(client.getTransactionStatusHistoryClient(), "TransactionStatusHistoryClient should not be null");
        assertNotNull(client.getTransactionLineInterestClient(), "TransactionLineInterestClient should not be null");
        assertNotNull(client.getTransactionLineCardClient(), "TransactionLineCardClient should not be null");
        assertNotNull(client.getTransactionLineDepositClient(), "TransactionLineDepositClient should not be null");
        assertNotNull(client.getTransactionLineDirectDebitClient(), "TransactionLineDirectDebitClient should not be null");

        assertEquals(CUSTOM_BASE_URL, client.getConfig().getBaseUrl(),
                "Custom base URL should be used");
        assertEquals(CoreBankingLedgerClientConfig.DEFAULT_CONNECT_TIMEOUT_MS, client.getConfig().getConnectTimeoutMs(),
                "Default connect timeout should be used");
        assertEquals(CoreBankingLedgerClientConfig.DEFAULT_READ_TIMEOUT_MS, client.getConfig().getReadTimeoutMs(),
                "Default read timeout should be used");
        assertEquals(CoreBankingLedgerClientConfig.DEFAULT_WRITE_TIMEOUT_MS, client.getConfig().getWriteTimeoutMs(),
                "Default write timeout should be used");
        assertFalse(client.getConfig().isEnableLogging(), "Logging should be disabled by default");
    }

    /**
     * Test initialization with custom configuration.
     */
    @Test
    void testConfigConstructor() {
        CoreBankingLedgerClientConfig config = CoreBankingLedgerClientConfig.builder()
                .baseUrl(CUSTOM_BASE_URL)
                .connectTimeoutMs(10000)
                .readTimeoutMs(15000)
                .writeTimeoutMs(20000)
                .enableLogging(true)
                .build();

        CoreBankingLedgerClient client = new CoreBankingLedgerClient(config);

        assertNotNull(client.getWebClient(), "WebClient should not be null");
        assertNotNull(client.getConfig(), "Config should not be null");
        assertNotNull(client.getTransactionClient(), "TransactionClient should not be null");
        assertNotNull(client.getAccountLegClient(), "AccountLegClient should not be null");
        assertNotNull(client.getTransactionLegClient(), "TransactionLegClient should not be null");
        assertNotNull(client.getTransactionAttachmentClient(), "TransactionAttachmentClient should not be null");
        assertNotNull(client.getTransactionCategoryClient(), "TransactionCategoryClient should not be null");
        assertNotNull(client.getTransactionStatusHistoryClient(), "TransactionStatusHistoryClient should not be null");
        assertNotNull(client.getTransactionLineInterestClient(), "TransactionLineInterestClient should not be null");
        assertNotNull(client.getTransactionLineCardClient(), "TransactionLineCardClient should not be null");
        assertNotNull(client.getTransactionLineDepositClient(), "TransactionLineDepositClient should not be null");
        assertNotNull(client.getTransactionLineDirectDebitClient(), "TransactionLineDirectDebitClient should not be null");

        assertEquals(CUSTOM_BASE_URL, client.getConfig().getBaseUrl(),
                "Custom base URL should be used");
        assertEquals(10000, client.getConfig().getConnectTimeoutMs(),
                "Custom connect timeout should be used");
        assertEquals(15000, client.getConfig().getReadTimeoutMs(),
                "Custom read timeout should be used");
        assertEquals(20000, client.getConfig().getWriteTimeoutMs(),
                "Custom write timeout should be used");
        assertTrue(client.getConfig().isEnableLogging(), "Logging should be enabled");
    }

    /**
     * Test initialization with custom WebClient.
     */
    @Test
    void testWebClientConstructor() {
        WebClient webClient = WebClient.builder().baseUrl(CUSTOM_BASE_URL).build();
        CoreBankingLedgerClient client = new CoreBankingLedgerClient(webClient);

        assertNotNull(client.getWebClient(), "WebClient should not be null");
        assertNull(client.getConfig(), "Config should be null");
        assertNotNull(client.getTransactionClient(), "TransactionClient should not be null");
        assertNotNull(client.getAccountLegClient(), "AccountLegClient should not be null");
        assertNotNull(client.getTransactionLegClient(), "TransactionLegClient should not be null");
        assertNotNull(client.getTransactionAttachmentClient(), "TransactionAttachmentClient should not be null");
        assertNotNull(client.getTransactionCategoryClient(), "TransactionCategoryClient should not be null");
        assertNotNull(client.getTransactionStatusHistoryClient(), "TransactionStatusHistoryClient should not be null");
        assertNotNull(client.getTransactionLineInterestClient(), "TransactionLineInterestClient should not be null");
        assertNotNull(client.getTransactionLineCardClient(), "TransactionLineCardClient should not be null");
        assertNotNull(client.getTransactionLineDepositClient(), "TransactionLineDepositClient should not be null");
        assertNotNull(client.getTransactionLineDirectDebitClient(), "TransactionLineDirectDebitClient should not be null");

        assertSame(webClient, client.getWebClient(), "Custom WebClient should be used");
    }
}
