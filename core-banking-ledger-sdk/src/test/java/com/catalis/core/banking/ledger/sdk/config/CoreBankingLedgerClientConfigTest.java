package com.catalis.core.banking.ledger.sdk.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the CoreBankingLedgerClientConfig class.
 */
class CoreBankingLedgerClientConfigTest {

    /**
     * Tests that the default configuration is created correctly.
     */
    @Test
    void testDefaultConfig() {
        CoreBankingLedgerClientConfig config = CoreBankingLedgerClientConfig.defaultConfig();
        
        assertEquals(CoreBankingLedgerClientConfig.DEFAULT_BASE_URL, config.getBaseUrl());
        assertEquals(CoreBankingLedgerClientConfig.DEFAULT_CONNECT_TIMEOUT_MS, config.getConnectTimeoutMs());
        assertEquals(CoreBankingLedgerClientConfig.DEFAULT_READ_TIMEOUT_MS, config.getReadTimeoutMs());
        assertEquals(CoreBankingLedgerClientConfig.DEFAULT_WRITE_TIMEOUT_MS, config.getWriteTimeoutMs());
        assertEquals(CoreBankingLedgerClientConfig.DEFAULT_MEMORY_SIZE_LIMIT, config.getMemorySizeLimit());
        assertFalse(config.isEnableLogging());
    }

    /**
     * Tests that a configuration with a custom base URL is created correctly.
     */
    @Test
    void testWithBaseUrl() {
        String baseUrl = "https://api.example.com";
        CoreBankingLedgerClientConfig config = CoreBankingLedgerClientConfig.withBaseUrl(baseUrl);
        
        assertEquals(baseUrl, config.getBaseUrl());
        assertEquals(CoreBankingLedgerClientConfig.DEFAULT_CONNECT_TIMEOUT_MS, config.getConnectTimeoutMs());
        assertEquals(CoreBankingLedgerClientConfig.DEFAULT_READ_TIMEOUT_MS, config.getReadTimeoutMs());
        assertEquals(CoreBankingLedgerClientConfig.DEFAULT_WRITE_TIMEOUT_MS, config.getWriteTimeoutMs());
        assertEquals(CoreBankingLedgerClientConfig.DEFAULT_MEMORY_SIZE_LIMIT, config.getMemorySizeLimit());
        assertFalse(config.isEnableLogging());
    }

    /**
     * Tests that a custom configuration is created correctly.
     */
    @Test
    void testCustomConfig() {
        String baseUrl = "https://api.example.com";
        int connectTimeout = 5000;
        int readTimeout = 6000;
        int writeTimeout = 7000;
        int memorySizeLimit = 8 * 1024 * 1024;
        boolean enableLogging = true;
        
        CoreBankingLedgerClientConfig config = CoreBankingLedgerClientConfig.builder()
                .baseUrl(baseUrl)
                .connectTimeoutMs(connectTimeout)
                .readTimeoutMs(readTimeout)
                .writeTimeoutMs(writeTimeout)
                .memorySizeLimit(memorySizeLimit)
                .enableLogging(enableLogging)
                .build();
        
        assertEquals(baseUrl, config.getBaseUrl());
        assertEquals(connectTimeout, config.getConnectTimeoutMs());
        assertEquals(readTimeout, config.getReadTimeoutMs());
        assertEquals(writeTimeout, config.getWriteTimeoutMs());
        assertEquals(memorySizeLimit, config.getMemorySizeLimit());
        assertTrue(config.isEnableLogging());
    }

    /**
     * Tests that a WebClient is created correctly.
     */
    @Test
    void testCreateWebClient() {
        CoreBankingLedgerClientConfig config = CoreBankingLedgerClientConfig.defaultConfig();
        WebClient webClient = config.createWebClient();
        
        assertNotNull(webClient);
    }
}
