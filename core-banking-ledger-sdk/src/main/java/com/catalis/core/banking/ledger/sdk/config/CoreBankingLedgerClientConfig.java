package com.catalis.core.banking.ledger.sdk.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Configuration class for the Core Banking Ledger SDK client.
 * <p>
 * This class provides configuration options for customizing the WebClient used by the SDK,
 * including base URL, timeouts, memory limits, and logging settings.
 */
@Data
@Builder
@Slf4j
public class CoreBankingLedgerClientConfig {

    /**
     * Default base URL for the Core Banking Ledger API.
     */
    public static final String DEFAULT_BASE_URL = "http://localhost:8080";

    /**
     * Default connection timeout in milliseconds.
     */
    public static final int DEFAULT_CONNECT_TIMEOUT_MS = 5000;

    /**
     * Default read timeout in milliseconds.
     */
    public static final int DEFAULT_READ_TIMEOUT_MS = 5000;

    /**
     * Default write timeout in milliseconds.
     */
    public static final int DEFAULT_WRITE_TIMEOUT_MS = 5000;

    /**
     * Default memory size limit in bytes.
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
     * Memory size limit in bytes for the WebClient.
     */
    @Builder.Default
    private int memorySizeLimit = DEFAULT_MEMORY_SIZE_LIMIT;

    /**
     * Flag to enable or disable request/response logging.
     */
    @Builder.Default
    private boolean enableLogging = false;

    /**
     * Creates a new WebClient instance with the configured settings.
     *
     * @return A configured WebClient instance.
     */
    public WebClient createWebClient() {
        // Configure HTTP client with timeouts
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutMs)
                .responseTimeout(Duration.ofMillis(readTimeoutMs))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(readTimeoutMs, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(writeTimeoutMs, TimeUnit.MILLISECONDS)));

        // Configure memory limits
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
     * Creates a filter function that logs request details.
     *
     * @return An ExchangeFilterFunction for logging requests.
     */
    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }

    /**
     * Creates a filter function that logs response details.
     *
     * @return An ExchangeFilterFunction for logging responses.
     */
    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("Response status: {}", clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }
}