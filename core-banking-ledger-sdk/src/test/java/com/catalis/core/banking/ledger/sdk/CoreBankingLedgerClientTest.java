package com.catalis.core.banking.ledger.sdk;

import com.catalis.core.banking.ledger.sdk.config.CoreBankingLedgerClientConfig;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for the CoreBankingLedgerClient class.
 */
class CoreBankingLedgerClientTest {

    /**
     * Tests that the client can be constructed with default configuration.
     */
    @Test
    void testConstructWithDefaultConfig() {
        CoreBankingLedgerClient client = new CoreBankingLedgerClient();

        assertNotNull(client.getWebClient());
        assertNotNull(client.getTransactionClient());
        assertNotNull(client.getTransactionLineCardClient());
        assertNotNull(client.getTransactionLineDirectDebitClient());
        assertNotNull(client.getTransactionLineStandingOrderClient());
        assertNotNull(client.getTransactionStatusHistoryClient());
        assertNotNull(client.getTransactionLineSepaTransferClient());
        assertNotNull(client.getTransactionLineWireTransferClient());
        assertNotNull(client.getTransactionLineDepositClient());
        assertNotNull(client.getTransactionLineWithdrawalClient());
        assertNotNull(client.getTransactionLineFeeClient());
        assertNotNull(client.getTransactionLineInterestClient());
        assertNotNull(client.getTransactionLineTransferClient());
        assertNotNull(client.getTransactionCategoryClient());
    }

    /**
     * Tests that the client can be constructed with a custom base URL.
     */
    @Test
    void testConstructWithBaseUrl() {
        String baseUrl = "https://api.example.com";
        CoreBankingLedgerClient client = new CoreBankingLedgerClient(baseUrl);

        assertNotNull(client.getWebClient());
        assertNotNull(client.getTransactionClient());
        assertNotNull(client.getTransactionLineCardClient());
        assertNotNull(client.getTransactionLineDirectDebitClient());
        assertNotNull(client.getTransactionLineStandingOrderClient());
        assertNotNull(client.getTransactionStatusHistoryClient());
        assertNotNull(client.getTransactionLineSepaTransferClient());
        assertNotNull(client.getTransactionLineWireTransferClient());
        assertNotNull(client.getTransactionLineDepositClient());
        assertNotNull(client.getTransactionLineWithdrawalClient());
        assertNotNull(client.getTransactionLineFeeClient());
        assertNotNull(client.getTransactionLineInterestClient());
        assertNotNull(client.getTransactionLineTransferClient());
        assertNotNull(client.getTransactionCategoryClient());
    }

    /**
     * Tests that the client can be constructed with a custom configuration.
     */
    @Test
    void testConstructWithConfig() {
        CoreBankingLedgerClientConfig config = CoreBankingLedgerClientConfig.builder()
                .baseUrl("https://api.example.com")
                .connectTimeoutMs(5000)
                .readTimeoutMs(5000)
                .writeTimeoutMs(5000)
                .enableLogging(true)
                .build();

        CoreBankingLedgerClient client = new CoreBankingLedgerClient(config);

        assertNotNull(client.getWebClient());
        assertNotNull(client.getTransactionClient());
        assertNotNull(client.getTransactionLineCardClient());
        assertNotNull(client.getTransactionLineDirectDebitClient());
        assertNotNull(client.getTransactionLineStandingOrderClient());
        assertNotNull(client.getTransactionStatusHistoryClient());
        assertNotNull(client.getTransactionLineSepaTransferClient());
        assertNotNull(client.getTransactionLineWireTransferClient());
        assertNotNull(client.getTransactionLineDepositClient());
        assertNotNull(client.getTransactionLineWithdrawalClient());
        assertNotNull(client.getTransactionLineFeeClient());
        assertNotNull(client.getTransactionLineInterestClient());
        assertNotNull(client.getTransactionLineTransferClient());
        assertNotNull(client.getTransactionCategoryClient());
    }

    /**
     * Tests that the client can be constructed with a custom WebClient.
     */
    @Test
    void testConstructWithWebClient() {
        WebClient webClient = mock(WebClient.class);
        CoreBankingLedgerClient client = new CoreBankingLedgerClient(webClient);

        assertSame(webClient, client.getWebClient());
        assertNotNull(client.getTransactionClient());
        assertNotNull(client.getTransactionLineCardClient());
        assertNotNull(client.getTransactionLineDirectDebitClient());
        assertNotNull(client.getTransactionLineStandingOrderClient());
        assertNotNull(client.getTransactionStatusHistoryClient());
        assertNotNull(client.getTransactionLineSepaTransferClient());
        assertNotNull(client.getTransactionLineWireTransferClient());
        assertNotNull(client.getTransactionLineDepositClient());
        assertNotNull(client.getTransactionLineWithdrawalClient());
        assertNotNull(client.getTransactionLineFeeClient());
        assertNotNull(client.getTransactionLineInterestClient());
        assertNotNull(client.getTransactionLineTransferClient());
        assertNotNull(client.getTransactionCategoryClient());
    }
}