package com.firefly.core.banking.ledger.core.services.core.v1;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.filters.FilterUtils;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.core.banking.ledger.core.mappers.core.v1.TransactionMapper;
import com.firefly.core.banking.ledger.core.mappers.core.v1.TransactionStatusHistoryMapper;
import com.firefly.core.banking.ledger.core.services.blockchain.v1.BlockchainService;
import com.firefly.core.banking.ledger.core.services.event.v1.EventOutboxService;
import com.firefly.core.banking.ledger.core.services.wallet.v1.WalletService;
import com.firefly.core.banking.ledger.interfaces.dtos.core.v1.TransactionDTO;
import com.firefly.core.banking.ledger.interfaces.dtos.crypto.v1.CryptoDepositDTO;
import com.firefly.core.banking.ledger.interfaces.dtos.crypto.v1.CryptoTransferDTO;
import com.firefly.core.banking.ledger.interfaces.dtos.crypto.v1.CryptoWithdrawalDTO;
import com.firefly.core.banking.ledger.interfaces.dtos.crypto.v1.NftTransferDTO;
import com.firefly.core.banking.ledger.interfaces.dtos.crypto.v1.TokenBurnDTO;
import com.firefly.core.banking.ledger.interfaces.dtos.crypto.v1.TokenMintDTO;
import com.firefly.core.banking.ledger.interfaces.enums.asset.v1.AssetTypeEnum;
import com.firefly.core.banking.ledger.interfaces.enums.core.v1.TransactionStatusEnum;
import com.firefly.core.banking.ledger.interfaces.enums.core.v1.TransactionTypeEnum;
import com.firefly.core.banking.ledger.models.entities.core.v1.Transaction;
import com.firefly.core.banking.ledger.models.entities.core.v1.TransactionStatusHistory;
import com.firefly.core.banking.ledger.models.repositories.core.v1.TransactionRepository;
import com.firefly.core.banking.ledger.models.repositories.core.v1.TransactionStatusHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Implementation of the TransactionService interface.
 * Provides functionality for managing financial transactions including CRUD operations
 * and various search and filtering capabilities.
 */
@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private TransactionStatusHistoryRepository statusHistoryRepository;

    @Autowired
    private TransactionMapper mapper;

    @Autowired
    private TransactionStatusHistoryMapper statusHistoryMapper;

    @Autowired
    private EventOutboxService eventOutboxService;
    
    @Autowired(required = false)
    private BlockchainService blockchainService;
    
    @Autowired(required = false)
    private WalletService walletService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<TransactionDTO> createTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = mapper.toEntity(transactionDTO);
        return repository.save(transaction)
                .flatMap(savedTransaction -> {
                    // Create initial status history record
                    TransactionStatusHistory statusHistory = new TransactionStatusHistory();
                    statusHistory.setTransactionId(savedTransaction.getTransactionId());
                    statusHistory.setStatusCode(savedTransaction.getTransactionStatus());
                    statusHistory.setStatusStartDatetime(LocalDateTime.now());
                    statusHistory.setReason("Initial transaction creation");
                    statusHistory.setRegulatedReportingFlag(false);

                    return statusHistoryRepository.save(statusHistory)
                            .then(Mono.just(savedTransaction));
                })
                .flatMap(savedTransaction -> {
                    // Publish transaction created event
                    return eventOutboxService.publishEvent(
                            "TRANSACTION",
                            savedTransaction.getTransactionId().toString(),
                            "TRANSACTION_CREATED",
                            mapper.toDTO(savedTransaction)
                    ).then(Mono.just(savedTransaction));
                })
                .map(mapper::toDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<TransactionDTO> getTransaction(Long transactionId) {
        return repository.findById(transactionId)
                .map(mapper::toDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<TransactionDTO> updateTransaction(Long transactionId, TransactionDTO transactionDTO) {
        return repository.findById(transactionId)
                .flatMap(existingTransaction -> {
                    TransactionStatusEnum oldStatus = existingTransaction.getTransactionStatus();
                    Transaction updatedTransaction = mapper.toEntity(transactionDTO);
                    updatedTransaction.setTransactionId(existingTransaction.getTransactionId());

                    return repository.save(updatedTransaction)
                            .flatMap(savedTransaction -> {
                                // If status has changed, create a status history record
                                if (!oldStatus.equals(savedTransaction.getTransactionStatus())) {
                                    TransactionStatusHistory statusHistory = new TransactionStatusHistory();
                                    statusHistory.setTransactionId(savedTransaction.getTransactionId());
                                    statusHistory.setStatusCode(savedTransaction.getTransactionStatus());
                                    statusHistory.setStatusStartDatetime(LocalDateTime.now());
                                    statusHistory.setReason("Status updated via API");
                                    statusHistory.setRegulatedReportingFlag(false);

                                    return statusHistoryRepository.save(statusHistory)
                                            .then(Mono.just(savedTransaction));
                                }
                                return Mono.just(savedTransaction);
                            })
                            .flatMap(savedTransaction -> {
                                // Publish transaction updated event
                                return eventOutboxService.publishEvent(
                                        "TRANSACTION",
                                        savedTransaction.getTransactionId().toString(),
                                        "TRANSACTION_UPDATED",
                                        mapper.toDTO(savedTransaction)
                                ).then(Mono.just(savedTransaction));
                            });
                })
                .map(mapper::toDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> deleteTransaction(Long transactionId) {
        return repository.findById(transactionId)
                .flatMap(transaction -> {
                    // Publish transaction deleted event
                    return eventOutboxService.publishEvent(
                            "TRANSACTION",
                            transaction.getTransactionId().toString(),
                            "TRANSACTION_DELETED",
                            mapper.toDTO(transaction)
                    ).then(repository.delete(transaction));
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<PaginationResponse<TransactionDTO>> filterTransactions(FilterRequest<TransactionDTO> filterRequest) {
        return FilterUtils
                .createFilter(
                        Transaction.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    @Override
    public Flux<TransactionDTO> getTransactionsByAccountId(Long accountId) {
        return repository.findByAccountId(accountId)
                .map(mapper::toDTO);
    }

    @Override
    public Flux<TransactionDTO> getTransactionsByAccountSpaceId(Long accountSpaceId) {
        return repository.findByAccountSpaceId(accountSpaceId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TransactionDTO> updateTransactionStatus(Long transactionId, TransactionStatusEnum newStatus, String reason) {
        return repository.findById(transactionId)
                .flatMap(transaction -> {
                    TransactionStatusEnum oldStatus = transaction.getTransactionStatus();

                    // Only update if status is different
                    if (oldStatus.equals(newStatus)) {
                        return Mono.just(transaction);
                    }

                    // Update transaction status
                    transaction.setTransactionStatus(newStatus);

                    return repository.save(transaction)
                            .flatMap(savedTransaction -> {
                                // Create status history record
                                TransactionStatusHistory statusHistory = new TransactionStatusHistory();
                                statusHistory.setTransactionId(savedTransaction.getTransactionId());
                                statusHistory.setStatusCode(newStatus);
                                statusHistory.setStatusStartDatetime(LocalDateTime.now());
                                statusHistory.setReason(reason);
                                statusHistory.setRegulatedReportingFlag(false);

                                return statusHistoryRepository.save(statusHistory)
                                        .then(Mono.just(savedTransaction));
                            })
                            .flatMap(savedTransaction -> {
                                // Publish transaction status changed event
                                return eventOutboxService.publishEvent(
                                        "TRANSACTION",
                                        savedTransaction.getTransactionId().toString(),
                                        "TRANSACTION_STATUS_CHANGED",
                                        mapper.toDTO(savedTransaction)
                                ).then(Mono.just(savedTransaction));
                            });
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TransactionDTO> createReversalTransaction(Long originalTransactionId, String reason) {
        return repository.findById(originalTransactionId)
                .flatMap(originalTransaction -> {
                    // Create reversal transaction
                    Transaction reversalTransaction = new Transaction();
                    reversalTransaction.setTransactionType(originalTransaction.getTransactionType());
                    reversalTransaction.setTransactionStatus(TransactionStatusEnum.PENDING);
                    reversalTransaction.setTotalAmount(originalTransaction.getTotalAmount().negate());
                    reversalTransaction.setCurrency(originalTransaction.getCurrency());
                    reversalTransaction.setDescription("Reversal: " + originalTransaction.getDescription());
                    reversalTransaction.setInitiatingParty(originalTransaction.getInitiatingParty());
                    reversalTransaction.setAccountId(originalTransaction.getAccountId());
                    reversalTransaction.setAccountSpaceId(originalTransaction.getAccountSpaceId());
                    reversalTransaction.setTransactionCategoryId(originalTransaction.getTransactionCategoryId());
                    reversalTransaction.setTransactionDate(LocalDateTime.now());
                    reversalTransaction.setValueDate(LocalDateTime.now());
                    reversalTransaction.setRelatedTransactionId(originalTransactionId);
                    reversalTransaction.setRelationType("REVERSAL");
                    reversalTransaction.setRequestId("REVERSAL-" + originalTransaction.getTransactionId());

                    return repository.save(reversalTransaction)
                            .flatMap(savedReversal -> {
                                // Create status history record
                                TransactionStatusHistory statusHistory = new TransactionStatusHistory();
                                statusHistory.setTransactionId(savedReversal.getTransactionId());
                                statusHistory.setStatusCode(savedReversal.getTransactionStatus());
                                statusHistory.setStatusStartDatetime(LocalDateTime.now());
                                statusHistory.setReason(reason);
                                statusHistory.setRegulatedReportingFlag(false);

                                return statusHistoryRepository.save(statusHistory)
                                        .then(Mono.just(savedReversal));
                            })
                            .flatMap(savedReversal -> {
                                // Publish reversal transaction created event
                                return eventOutboxService.publishEvent(
                                        "TRANSACTION",
                                        savedReversal.getTransactionId().toString(),
                                        "REVERSAL_TRANSACTION_CREATED",
                                        mapper.toDTO(savedReversal)
                                ).then(Mono.just(savedReversal));
                            });
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TransactionDTO> findByExternalReference(String externalReference) {
        return repository.findByExternalReference(externalReference)
                .map(mapper::toDTO);
    }
    
    @Override
    public Mono<TransactionDTO> createCryptoDeposit(CryptoDepositDTO depositDTO) {
        // Create a new transaction for the crypto deposit
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionTypeEnum.CRYPTO_DEPOSIT);
        transaction.setTransactionStatus(TransactionStatusEnum.PENDING);
        transaction.setTotalAmount(depositDTO.getAmount());
        transaction.setCurrency(depositDTO.getAssetSymbol());
        transaction.setDescription(depositDTO.getDescription());
        transaction.setAccountId(depositDTO.getAccountId());
        transaction.setAccountSpaceId(depositDTO.getAccountSpaceId());
        transaction.setTransactionDate(depositDTO.getTransactionDate() != null ? 
                depositDTO.getTransactionDate() : LocalDateTime.now());
        transaction.setValueDate(depositDTO.getValueDate() != null ? 
                depositDTO.getValueDate() : LocalDateTime.now());
        transaction.setExternalReference(depositDTO.getExternalReference());
        
        // Set crypto-specific fields
        transaction.setAssetType(AssetTypeEnum.CRYPTOCURRENCY);
        transaction.setBlockchainNetworkId(depositDTO.getBlockchainNetworkId());
        transaction.setBlockchainTransactionHash(depositDTO.getBlockchainTransactionHash());
        transaction.setCryptoAddressRiskScore(0); // Default value, should be calculated based on address risk
        
        return repository.save(transaction)
                .flatMap(savedTransaction -> {
                    // Create initial status history record
                    TransactionStatusHistory statusHistory = new TransactionStatusHistory();
                    statusHistory.setTransactionId(savedTransaction.getTransactionId());
                    statusHistory.setStatusCode(savedTransaction.getTransactionStatus());
                    statusHistory.setStatusStartDatetime(LocalDateTime.now());
                    statusHistory.setReason("Initial crypto deposit creation");
                    statusHistory.setRegulatedReportingFlag(false);

                    return statusHistoryRepository.save(statusHistory)
                            .then(Mono.just(savedTransaction));
                })
                .flatMap(savedTransaction -> {
                    // Publish transaction created event
                    return eventOutboxService.publishEvent(
                            "TRANSACTION",
                            savedTransaction.getTransactionId().toString(),
                            "CRYPTO_DEPOSIT_CREATED",
                            mapper.toDTO(savedTransaction)
                    ).then(Mono.just(savedTransaction));
                })
                .map(mapper::toDTO);
    }
    
    @Override
    public Mono<TransactionDTO> createCryptoWithdrawal(CryptoWithdrawalDTO withdrawalDTO) {
        // Validate the recipient address if wallet service is available
        Mono<Boolean> addressValidation = walletService != null ?
                walletService.validateAddress(withdrawalDTO.getRecipientAddress(), withdrawalDTO.getCryptoAssetId()) :
                Mono.just(true);
        
        return addressValidation
                .flatMap(isValid -> {
                    if (!isValid) {
                        return Mono.error(new IllegalArgumentException("Invalid recipient address"));
                    }
                    
                    // Create a new transaction for the crypto withdrawal
                    Transaction transaction = new Transaction();
                    transaction.setTransactionType(TransactionTypeEnum.CRYPTO_WITHDRAWAL);
                    transaction.setTransactionStatus(TransactionStatusEnum.PENDING);
                    transaction.setTotalAmount(withdrawalDTO.getAmount().negate()); // Negative amount for withdrawals
                    transaction.setCurrency(withdrawalDTO.getAssetSymbol());
                    transaction.setDescription(withdrawalDTO.getDescription());
                    transaction.setAccountId(withdrawalDTO.getAccountId());
                    transaction.setAccountSpaceId(withdrawalDTO.getAccountSpaceId());
                    transaction.setTransactionDate(withdrawalDTO.getTransactionDate() != null ? 
                            withdrawalDTO.getTransactionDate() : LocalDateTime.now());
                    transaction.setValueDate(withdrawalDTO.getValueDate() != null ? 
                            withdrawalDTO.getValueDate() : LocalDateTime.now());
                    transaction.setExternalReference(withdrawalDTO.getExternalReference());
                    
                    // Set crypto-specific fields
                    transaction.setAssetType(AssetTypeEnum.CRYPTOCURRENCY);
                    transaction.setBlockchainNetworkId(withdrawalDTO.getBlockchainNetworkId());
                    
                    return repository.save(transaction)
                            .flatMap(savedTransaction -> {
                                // Create initial status history record
                                TransactionStatusHistory statusHistory = new TransactionStatusHistory();
                                statusHistory.setTransactionId(savedTransaction.getTransactionId());
                                statusHistory.setStatusCode(savedTransaction.getTransactionStatus());
                                statusHistory.setStatusStartDatetime(LocalDateTime.now());
                                statusHistory.setReason("Initial crypto withdrawal creation");
                                statusHistory.setRegulatedReportingFlag(false);

                                return statusHistoryRepository.save(statusHistory)
                                        .then(Mono.just(savedTransaction));
                            })
                            .flatMap(savedTransaction -> {
                                // Send the transaction to the blockchain if blockchain service is available
                                if (blockchainService != null && withdrawalDTO.getSenderAddress() != null) {
                                    return blockchainService.sendTransaction(
                                            withdrawalDTO.getSenderAddress(),
                                            withdrawalDTO.getRecipientAddress(),
                                            withdrawalDTO.getAmount(),
                                            withdrawalDTO.getCryptoAssetId()
                                    )
                                    .flatMap(txHash -> {
                                        savedTransaction.setBlockchainTransactionHash(txHash);
                                        return repository.save(savedTransaction);
                                    });
                                }
                                return Mono.just(savedTransaction);
                            })
                            .flatMap(savedTransaction -> {
                                // Publish transaction created event
                                return eventOutboxService.publishEvent(
                                        "TRANSACTION",
                                        savedTransaction.getTransactionId().toString(),
                                        "CRYPTO_WITHDRAWAL_CREATED",
                                        mapper.toDTO(savedTransaction)
                                ).then(Mono.just(savedTransaction));
                            })
                            .map(mapper::toDTO);
                });
    }
    
    @Override
    public Mono<TransactionDTO> createCryptoTransfer(CryptoTransferDTO transferDTO) {
        // Generate destination address if wallet service is available and on-chain transaction is required
        Mono<String> destinationAddressMono;
        if (transferDTO.getRequiresOnChainTransaction() != null && 
            transferDTO.getRequiresOnChainTransaction() && 
            walletService != null && 
            transferDTO.getRecipientAddress() == null) {
            destinationAddressMono = walletService.generateAddress(
                    transferDTO.getDestinationAccountId(), 
                    transferDTO.getCryptoAssetId()
            );
        } else if (transferDTO.getRecipientAddress() != null) {
            destinationAddressMono = Mono.just(transferDTO.getRecipientAddress());
        } else {
            // If no recipient address is provided and we can't generate one, use a default empty string
            // In a real implementation, you would handle this differently
            destinationAddressMono = Mono.just("");
        }
        
        return destinationAddressMono
                .flatMap(destinationAddress -> {
                    // Create a new transaction for the crypto transfer
                    Transaction transaction = new Transaction();
                    transaction.setTransactionType(TransactionTypeEnum.CRYPTO_TRANSFER);
                    transaction.setTransactionStatus(TransactionStatusEnum.PENDING);
                    transaction.setTotalAmount(transferDTO.getAmount());
                    transaction.setCurrency(transferDTO.getAssetSymbol());
                    transaction.setDescription(transferDTO.getDescription());
                    transaction.setAccountId(transferDTO.getSourceAccountId());
                    transaction.setAccountSpaceId(transferDTO.getSourceAccountSpaceId());
                    transaction.setTransactionDate(transferDTO.getTransactionDate() != null ? 
                            transferDTO.getTransactionDate() : LocalDateTime.now());
                    transaction.setValueDate(transferDTO.getValueDate() != null ? 
                            transferDTO.getValueDate() : LocalDateTime.now());
                    transaction.setExternalReference(transferDTO.getExternalReference());
                    transaction.setRelatedTransactionId(transferDTO.getRelatedTransactionId());
                    
                    // Set crypto-specific fields
                    transaction.setAssetType(AssetTypeEnum.CRYPTOCURRENCY);
                    transaction.setBlockchainNetworkId(transferDTO.getBlockchainNetworkId());
                    
                    return repository.save(transaction)
                            .flatMap(savedTransaction -> {
                                // Create initial status history record
                                TransactionStatusHistory statusHistory = new TransactionStatusHistory();
                                statusHistory.setTransactionId(savedTransaction.getTransactionId());
                                statusHistory.setStatusCode(savedTransaction.getTransactionStatus());
                                statusHistory.setStatusStartDatetime(LocalDateTime.now());
                                statusHistory.setReason("Initial crypto transfer creation");
                                statusHistory.setRegulatedReportingFlag(false);

                                return statusHistoryRepository.save(statusHistory)
                                        .then(Mono.just(savedTransaction));
                            })
                            .flatMap(savedTransaction -> {
                                // If on-chain transaction is required and blockchain service is available
                                if (transferDTO.getRequiresOnChainTransaction() != null && 
                                    transferDTO.getRequiresOnChainTransaction() && 
                                    blockchainService != null && 
                                    transferDTO.getSenderAddress() != null) {
                                    return blockchainService.sendTransaction(
                                            transferDTO.getSenderAddress(),
                                            destinationAddress,
                                            transferDTO.getAmount(),
                                            transferDTO.getCryptoAssetId()
                                    )
                                    .flatMap(txHash -> {
                                        savedTransaction.setBlockchainTransactionHash(txHash);
                                        return repository.save(savedTransaction);
                                    });
                                }
                                return Mono.just(savedTransaction);
                            })
                            .flatMap(savedTransaction -> {
                                // Publish transaction created event
                                return eventOutboxService.publishEvent(
                                        "TRANSACTION",
                                        savedTransaction.getTransactionId().toString(),
                                        "CRYPTO_TRANSFER_CREATED",
                                        mapper.toDTO(savedTransaction)
                                ).then(Mono.just(savedTransaction));
                            })
                            .map(mapper::toDTO);
                });
    }
    
    @Override
    public Mono<TransactionDTO> createTokenMint(TokenMintDTO mintDTO) {
        // Create a new transaction for the token mint
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionTypeEnum.TOKEN_MINT);
        transaction.setTransactionStatus(TransactionStatusEnum.PENDING);
        transaction.setTotalAmount(mintDTO.getAmount());
        transaction.setCurrency(mintDTO.getAssetSymbol());
        transaction.setDescription(mintDTO.getDescription());
        transaction.setAccountId(mintDTO.getAccountId());
        transaction.setAccountSpaceId(mintDTO.getAccountSpaceId());
        transaction.setTransactionDate(mintDTO.getTransactionDate() != null ? 
                mintDTO.getTransactionDate() : LocalDateTime.now());
        transaction.setValueDate(mintDTO.getValueDate() != null ? 
                mintDTO.getValueDate() : LocalDateTime.now());
        transaction.setExternalReference(mintDTO.getExternalReference());
        
        // Set crypto-specific fields
        transaction.setAssetType(AssetTypeEnum.TOKEN_SECURITY);
        transaction.setBlockchainNetworkId(mintDTO.getBlockchainNetworkId());
        
        return repository.save(transaction)
                .flatMap(savedTransaction -> {
                    // Create initial status history record
                    TransactionStatusHistory statusHistory = new TransactionStatusHistory();
                    statusHistory.setTransactionId(savedTransaction.getTransactionId());
                    statusHistory.setStatusCode(savedTransaction.getTransactionStatus());
                    statusHistory.setStatusStartDatetime(LocalDateTime.now());
                    statusHistory.setReason("Initial token mint creation");
                    statusHistory.setRegulatedReportingFlag(false);

                    return statusHistoryRepository.save(statusHistory)
                            .then(Mono.just(savedTransaction));
                })
                .flatMap(savedTransaction -> {
                    // Publish transaction created event
                    return eventOutboxService.publishEvent(
                            "TRANSACTION",
                            savedTransaction.getTransactionId().toString(),
                            "TOKEN_MINT_CREATED",
                            mapper.toDTO(savedTransaction)
                    ).then(Mono.just(savedTransaction));
                })
                .map(mapper::toDTO);
    }
    
    @Override
    public Mono<TransactionDTO> createTokenBurn(TokenBurnDTO burnDTO) {
        // Create a new transaction for the token burn
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionTypeEnum.TOKEN_BURN);
        transaction.setTransactionStatus(TransactionStatusEnum.PENDING);
        transaction.setTotalAmount(burnDTO.getAmount().negate()); // Negative amount for burns
        transaction.setCurrency(burnDTO.getAssetSymbol());
        transaction.setDescription(burnDTO.getDescription());
        transaction.setAccountId(burnDTO.getAccountId());
        transaction.setAccountSpaceId(burnDTO.getAccountSpaceId());
        transaction.setTransactionDate(burnDTO.getTransactionDate() != null ? 
                burnDTO.getTransactionDate() : LocalDateTime.now());
        transaction.setValueDate(burnDTO.getValueDate() != null ? 
                burnDTO.getValueDate() : LocalDateTime.now());
        transaction.setExternalReference(burnDTO.getExternalReference());
        
        // Set crypto-specific fields
        transaction.setAssetType(AssetTypeEnum.TOKEN_SECURITY);
        transaction.setBlockchainNetworkId(burnDTO.getBlockchainNetworkId());
        
        return repository.save(transaction)
                .flatMap(savedTransaction -> {
                    // Create initial status history record
                    TransactionStatusHistory statusHistory = new TransactionStatusHistory();
                    statusHistory.setTransactionId(savedTransaction.getTransactionId());
                    statusHistory.setStatusCode(savedTransaction.getTransactionStatus());
                    statusHistory.setStatusStartDatetime(LocalDateTime.now());
                    statusHistory.setReason("Initial token burn creation");
                    statusHistory.setRegulatedReportingFlag(false);

                    return statusHistoryRepository.save(statusHistory)
                            .then(Mono.just(savedTransaction));
                })
                .flatMap(savedTransaction -> {
                    // Publish transaction created event
                    return eventOutboxService.publishEvent(
                            "TRANSACTION",
                            savedTransaction.getTransactionId().toString(),
                            "TOKEN_BURN_CREATED",
                            mapper.toDTO(savedTransaction)
                    ).then(Mono.just(savedTransaction));
                })
                .map(mapper::toDTO);
    }
    
    @Override
    public Mono<TransactionDTO> createNftTransfer(NftTransferDTO transferDTO) {
        // Create a new transaction for the NFT transfer
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionTypeEnum.NFT_TRANSFER);
        transaction.setTransactionStatus(TransactionStatusEnum.PENDING);
        transaction.setTotalAmount(transferDTO.getAmount() != null ? transferDTO.getAmount() : new BigDecimal("1.0"));
        transaction.setCurrency(transferDTO.getAssetSymbol());
        transaction.setDescription(transferDTO.getDescription());
        transaction.setAccountId(transferDTO.getAccountId());
        transaction.setAccountSpaceId(transferDTO.getAccountSpaceId());
        transaction.setTransactionDate(transferDTO.getTransactionDate() != null ? 
                transferDTO.getTransactionDate() : LocalDateTime.now());
        transaction.setValueDate(transferDTO.getValueDate() != null ? 
                transferDTO.getValueDate() : LocalDateTime.now());
        transaction.setExternalReference(transferDTO.getExternalReference());
        
        // Set crypto-specific fields
        transaction.setAssetType(AssetTypeEnum.TOKEN_NFT);
        transaction.setBlockchainNetworkId(transferDTO.getBlockchainNetworkId());
        
        return repository.save(transaction)
                .flatMap(savedTransaction -> {
                    // Create initial status history record
                    TransactionStatusHistory statusHistory = new TransactionStatusHistory();
                    statusHistory.setTransactionId(savedTransaction.getTransactionId());
                    statusHistory.setStatusCode(savedTransaction.getTransactionStatus());
                    statusHistory.setStatusStartDatetime(LocalDateTime.now());
                    statusHistory.setReason("Initial NFT transfer creation");
                    statusHistory.setRegulatedReportingFlag(false);

                    return statusHistoryRepository.save(statusHistory)
                            .then(Mono.just(savedTransaction));
                })
                .flatMap(savedTransaction -> {
                    // Publish transaction created event
                    return eventOutboxService.publishEvent(
                            "TRANSACTION",
                            savedTransaction.getTransactionId().toString(),
                            "NFT_TRANSFER_CREATED",
                            mapper.toDTO(savedTransaction)
                    ).then(Mono.just(savedTransaction));
                })
                .map(mapper::toDTO);
    }
    
    @Override
    public Flux<TransactionDTO> getTransactionsByBlockchainHash(String blockchainTransactionHash) {
        // This method would typically use a repository method to find transactions by blockchain hash
        // Since the repository method might not exist yet, we'll implement a simple version
        return repository.findAll()
                .filter(transaction -> blockchainTransactionHash.equals(transaction.getBlockchainTransactionHash()))
                .map(mapper::toDTO);
    }
    
    @Override
    public Flux<TransactionDTO> getTransactionsByCryptoAssetId(Long cryptoAssetId) {
        // Since there's no direct cryptoAssetId field in the Transaction entity,
        // we'll use a combination of assetType and currency to identify crypto assets
        // In a real implementation, you would need a mapping between cryptoAssetId and currency/symbol
        // For now, we'll just filter non-FIAT transactions
        return repository.findAll()
                .filter(transaction -> transaction.getAssetType() != AssetTypeEnum.FIAT)
                // In a real implementation, you would add additional filtering based on cryptoAssetId
                // For example, if you have a mapping service that can convert cryptoAssetId to currency symbol
                .map(mapper::toDTO);
    }
    
    @Override
    public Mono<TransactionDTO> updateBlockchainTransactionHash(Long transactionId, String blockchainTransactionHash) {
        return repository.findById(transactionId)
                .flatMap(transaction -> {
                    transaction.setBlockchainTransactionHash(blockchainTransactionHash);
                    return repository.save(transaction);
                })
                .map(mapper::toDTO);
    }
    
    @Override
    public Mono<TransactionDTO> updateConfirmationCount(Long transactionId, Integer confirmationCount) {
        return repository.findById(transactionId)
                .flatMap(transaction -> {
                    // Update confirmation count logic would go here
                    // This might involve updating a field in the transaction entity
                    // For now, we'll just update the transaction status based on confirmation count
                    if (confirmationCount >= 6) {
                        transaction.setTransactionStatus(TransactionStatusEnum.POSTED);
                    }
                    return repository.save(transaction);
                })
                .map(mapper::toDTO);
    }
}