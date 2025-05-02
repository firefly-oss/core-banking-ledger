package com.catalis.core.banking.ledger.sdk.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Configuration class for the Core Banking Ledger SDK client.
 * Provides options to customize the WebClient used for API communication.
 */
@Data
@Builder
public class CoreBankingLedgerClientConfig {

    /**
     * Default base URL for the Core Banking Ledger API.
     */
    public static final String DEFAULT_BASE_URL = "http://localhost:8080";

    /**
     * Default connection timeout in milliseconds.
     */
    public static final int DEFAULT_CONNECT_TIMEOUT_MS = 10000;

    /**
     * Default read timeout in milliseconds.
     */
    public static final int DEFAULT_READ_TIMEOUT_MS = 10000;

    /**
     * Default write timeout in milliseconds.
     */
    public static final int DEFAULT_WRITE_TIMEOUT_MS = 10000;

    /**
     * Default memory size limit for the WebClient in bytes.
     */
    public static final int DEFAULT_MEMORY_SIZE_LIMIT = 16 * 1024 * 1024; // 16MB

    /**
     * Base URL for the Core Banking Ledger API.
     */
    @Builder.Default
    private String baseUrl = DEFAULT_BASE_URL;

    /**
     * Connection timeout in milliseconds.
     */
    @Builder.Default
    private int connectTimeoutMs = DEFAULT_CONNECT_TIMEOUT_MS;

    /**
     * Read timeout in milliseconds.
     */
    @Builder.Default
    private int readTimeoutMs = DEFAULT_READ_TIMEOUT_MS;

    /**
     * Write timeout in milliseconds.
     */
    @Builder.Default
    private int writeTimeoutMs = DEFAULT_WRITE_TIMEOUT_MS;

    /**
     * Memory size limit for the WebClient in bytes.
     */
    @Builder.Default
    private int memorySizeLimit = DEFAULT_MEMORY_SIZE_LIMIT;

    /**
     * Flag to enable logging of requests and responses.
     */
    @Builder.Default
    private boolean enableLogging = false;

    /**
     * Maximum number of connections in the connection pool.
     */
    @Builder.Default
    private int maxConnections = 500;

    /**
     * Maximum connection idle time in milliseconds.
     */
    @Builder.Default
    private long maxIdleTimeMs = 30000;

    /**
     * Creates a new WebClient instance with the configured settings.
     *
     * @return A configured WebClient instance.
     */
    public WebClient createWebClient() {
        // Configure connection provider
        ConnectionProvider connectionProvider = ConnectionProvider.builder("core-banking-ledger-connection-pool")
                .maxConnections(maxConnections)
                .maxIdleTime(Duration.ofMillis(maxIdleTimeMs))
                .build();

        // Configure HTTP client with timeouts
        HttpClient httpClient = HttpClient.create(connectionProvider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutMs)
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(readTimeoutMs, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(writeTimeoutMs, TimeUnit.MILLISECONDS)));

        // Configure exchange strategies for memory limits
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(memorySizeLimit))
                .build();

        // Create WebClient builder
        WebClient.Builder webClientBuilder = WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(exchangeStrategies);

        // Add logging filter if enabled
        if (enableLogging) {
            webClientBuilder.filter(logRequest());
            webClientBuilder.filter(logResponse());
        }

        return webClientBuilder.build();
    }

    /**
     * Creates a filter function to log request details.
     *
     * @return An ExchangeFilterFunction for logging requests.
     */
    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            System.out.println("Request: " + clientRequest.method() + " " + clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> System.out.println(name + ": " + value)));
            return Mono.just(clientRequest);
        });
    }

    /**
     * Creates a filter function to log response details.
     *
     * @return An ExchangeFilterFunction for logging responses.
     */
    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            System.out.println("Response status: " + clientResponse.statusCode());
            clientResponse.headers().asHttpHeaders().forEach((name, values) -> values.forEach(value -> System.out.println(name + ": " + value)));
            return Mono.just(clientResponse);
        });
    }

    /**
     * Creates a default configuration.
     *
     * @return A default CoreBankingLedgerClientConfig instance.
     */
    public static CoreBankingLedgerClientConfig defaultConfig() {
        return CoreBankingLedgerClientConfig.builder().build();
    }

    /**
     * Creates a configuration with a custom base URL.
     *
     * @param baseUrl The base URL for the Core Banking Ledger API.
     * @return A CoreBankingLedgerClientConfig instance with the specified base URL.
     */
    public static CoreBankingLedgerClientConfig withBaseUrl(String baseUrl) {
        return CoreBankingLedgerClientConfig.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
