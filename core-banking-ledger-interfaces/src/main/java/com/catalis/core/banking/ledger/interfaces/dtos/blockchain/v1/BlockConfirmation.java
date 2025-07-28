package com.catalis.core.banking.ledger.interfaces.dtos.blockchain.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object representing blockchain transaction confirmation details.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockConfirmation {
    
    /**
     * The blockchain transaction hash
     */
    private String transactionHash;
    
    /**
     * The block number in which the transaction was included
     */
    private Long blockNumber;
    
    /**
     * The timestamp when the block was mined
     */
    private LocalDateTime blockTimestamp;
    
    /**
     * The number of confirmations (blocks mined after the transaction's block)
     */
    private Integer confirmationCount;
    
    /**
     * The current blockchain network height (latest block number)
     */
    private Long currentBlockHeight;
    
    /**
     * Whether the transaction has reached the required number of confirmations
     * to be considered final
     */
    private Boolean isFinal;
    
    /**
     * The number of confirmations required for the transaction to be considered final
     */
    private Integer requiredConfirmations;
}