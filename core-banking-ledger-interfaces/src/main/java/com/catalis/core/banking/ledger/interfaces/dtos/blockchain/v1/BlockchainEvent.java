package com.catalis.core.banking.ledger.interfaces.dtos.blockchain.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Data Transfer Object representing an event emitted by a smart contract on the blockchain.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockchainEvent {
    
    /**
     * The blockchain network ID
     */
    private Long blockchainNetworkId;
    
    /**
     * The address of the smart contract that emitted the event
     */
    private String contractAddress;
    
    /**
     * The name of the event
     */
    private String eventName;
    
    /**
     * The transaction hash in which the event was emitted
     */
    private String transactionHash;
    
    /**
     * The block number in which the event was emitted
     */
    private Long blockNumber;
    
    /**
     * The timestamp when the block was mined
     */
    private LocalDateTime blockTimestamp;
    
    /**
     * The log index of the event in the transaction
     */
    private Integer logIndex;
    
    /**
     * The parameters of the event as key-value pairs
     * The key is the parameter name, and the value is the parameter value
     */
    private Map<String, Object> parameters;
    
    /**
     * The raw data of the event
     */
    private String rawData;
    
    /**
     * The topics of the event (for indexed parameters)
     */
    private String[] topics;
    
    /**
     * The address that initiated the transaction
     */
    private String fromAddress;
    
    /**
     * The address that received the transaction
     */
    private String toAddress;
    
    /**
     * The event signature (hash of the event name and parameter types)
     */
    private String eventSignature;
    
    /**
     * Flag indicating whether this event has been processed
     */
    private Boolean processed;
    
    /**
     * The timestamp when the event was processed
     */
    private LocalDateTime processedTimestamp;
    
    /**
     * The ID of the transaction created as a result of processing this event
     */
    private Long resultingTransactionId;
}