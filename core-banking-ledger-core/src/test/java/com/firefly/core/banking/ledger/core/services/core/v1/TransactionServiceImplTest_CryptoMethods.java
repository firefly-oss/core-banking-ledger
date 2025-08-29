package com.firefly.core.banking.ledger.core.services.core.v1;

import com.firefly.core.banking.ledger.core.mappers.core.v1.TransactionMapper;
import com.firefly.core.banking.ledger.core.mappers.core.v1.TransactionStatusHistoryMapper;
import com.firefly.core.banking.ledger.core.services.blockchain.v1.BlockchainService;
import com.firefly.core.banking.ledger.core.services.event.v1.EventOutboxService;
import com.firefly.core.banking.ledger.core.services.wallet.v1.WalletService;
import com.firefly.core.banking.ledger.interfaces.dtos.blockchain.v1.BlockConfirmation;
import com.firefly.core.banking.ledger.interfaces.dtos.core.v1.TransactionDTO;
import com.firefly.core.banking.ledger.interfaces.dtos.crypto.v1.BaseCryptoTransactionDTO;
import com.firefly.core.banking.ledger.interfaces.dtos.crypto.v1.CryptoDepositDTO;
import com.firefly.core.banking.ledger.interfaces.dtos.crypto.v1.CryptoTransferDTO;
import com.firefly.core.banking.ledger.interfaces.dtos.crypto.v1.CryptoWithdrawalDTO;
import com.firefly.core.banking.ledger.interfaces.dtos.crypto.v1.NftTransferDTO;
import com.firefly.core.banking.ledger.interfaces.dtos.crypto.v1.TokenBurnDTO;
import com.firefly.core.banking.ledger.interfaces.dtos.crypto.v1.TokenMintDTO;
import com.firefly.core.banking.ledger.interfaces.enums.asset.v1.AssetTypeEnum;
import com.firefly.core.banking.ledger.interfaces.enums.blockchain.v1.TransactionStatus;
import com.firefly.core.banking.ledger.interfaces.enums.core.v1.TransactionStatusEnum;
import com.firefly.core.banking.ledger.interfaces.enums.core.v1.TransactionTypeEnum;
import com.firefly.core.banking.ledger.models.entities.core.v1.Transaction;
import com.firefly.core.banking.ledger.models.entities.core.v1.TransactionStatusHistory;
import com.firefly.core.banking.ledger.models.repositories.core.v1.TransactionRepository;
import com.firefly.core.banking.ledger.models.repositories.core.v1.TransactionStatusHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest_CryptoMethods {

    @Mock
    private TransactionRepository repository;

    @Mock
    private TransactionStatusHistoryRepository statusHistoryRepository;

    @Mock
    private TransactionMapper mapper;

    @Mock
    private TransactionStatusHistoryMapper statusHistoryMapper;

    @Mock
    private EventOutboxService eventOutboxService;

    @Mock
    private BlockchainService blockchainService;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private TransactionServiceImpl service;

    private TransactionDTO transactionDTO;
    private Transaction transaction;
    private TransactionStatusHistory statusHistory;
    private CryptoDepositDTO cryptoDepositDTO;
    private CryptoWithdrawalDTO cryptoWithdrawalDTO;
    private CryptoTransferDTO cryptoTransferDTO;
    private TokenMintDTO tokenMintDTO;
    private TokenBurnDTO tokenBurnDTO;
    private NftTransferDTO nftTransferDTO;
    private final Long transactionId = 1L;
    private final Long cryptoAssetId = 100L;
    private final Long blockchainNetworkId = 1L;
    private final String blockchainTransactionHash = "0x1234567890abcdef";
    private final String walletAddress = "0xabcdef1234567890";
    private final String destinationAddress = "0x0987654321fedcba";

    @BeforeEach
    void setUp() {
        // Initialize common test data
        transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionId(transactionId);
        transactionDTO.setTotalAmount(new BigDecimal("1.0"));
        transactionDTO.setDescription("Test Crypto Transaction");
        transactionDTO.setTransactionDate(LocalDateTime.now());
        transactionDTO.setValueDate(LocalDateTime.now());
        transactionDTO.setTransactionStatus(TransactionStatusEnum.PENDING);
        transactionDTO.setCurrency("BTC");
        transactionDTO.setAccountId(100L);

        transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setTotalAmount(new BigDecimal("1.0"));
        transaction.setDescription("Test Crypto Transaction");
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setValueDate(LocalDateTime.now());
        transaction.setTransactionStatus(TransactionStatusEnum.PENDING);
        transaction.setCurrency("BTC");
        transaction.setAccountId(100L);
        transaction.setAssetType(AssetTypeEnum.CRYPTOCURRENCY);
        transaction.setBlockchainNetworkId(blockchainNetworkId);
        transaction.setBlockchainTransactionHash(blockchainTransactionHash);

        statusHistory = new TransactionStatusHistory();
        statusHistory.setTransactionStatusHistoryId(1L);
        statusHistory.setTransactionId(transactionId);
        statusHistory.setStatusCode(TransactionStatusEnum.PENDING);
        statusHistory.setStatusStartDatetime(LocalDateTime.now());
        statusHistory.setReason("Initial transaction creation");
        statusHistory.setRegulatedReportingFlag(false);

        // Initialize crypto-specific DTOs
        setupCryptoDepositDTO();
        setupCryptoWithdrawalDTO();
        setupCryptoTransferDTO();
        setupTokenMintDTO();
        setupTokenBurnDTO();
        setupNftTransferDTO();
    }

    private void setupCryptoDepositDTO() {
        cryptoDepositDTO = new CryptoDepositDTO();
        cryptoDepositDTO.setAccountId(100L);
        cryptoDepositDTO.setAmount(new BigDecimal("1.0"));
        cryptoDepositDTO.setCryptoAssetId(cryptoAssetId);
        cryptoDepositDTO.setBlockchainNetworkId(blockchainNetworkId);
        cryptoDepositDTO.setRecipientAddress(walletAddress);
        cryptoDepositDTO.setBlockchainTransactionHash(blockchainTransactionHash);
        cryptoDepositDTO.setDescription("Test Crypto Deposit");
    }

    private void setupCryptoWithdrawalDTO() {
        cryptoWithdrawalDTO = new CryptoWithdrawalDTO();
        cryptoWithdrawalDTO.setAccountId(100L);
        cryptoWithdrawalDTO.setAmount(new BigDecimal("1.0"));
        cryptoWithdrawalDTO.setCryptoAssetId(cryptoAssetId);
        cryptoWithdrawalDTO.setBlockchainNetworkId(blockchainNetworkId);
        cryptoWithdrawalDTO.setRecipientAddress(destinationAddress);
        cryptoWithdrawalDTO.setSenderAddress(walletAddress); // Add sender address
        cryptoWithdrawalDTO.setDescription("Test Crypto Withdrawal");
    }

    private void setupCryptoTransferDTO() {
        cryptoTransferDTO = new CryptoTransferDTO();
        cryptoTransferDTO.setSourceAccountId(100L);
        cryptoTransferDTO.setDestinationAccountId(200L);
        cryptoTransferDTO.setAmount(new BigDecimal("1.0"));
        cryptoTransferDTO.setCryptoAssetId(cryptoAssetId);
        cryptoTransferDTO.setBlockchainNetworkId(blockchainNetworkId);
        cryptoTransferDTO.setRequiresOnChainTransaction(true); // Set requires on-chain transaction flag
        cryptoTransferDTO.setSenderAddress(walletAddress); // Add sender address
        cryptoTransferDTO.setDescription("Test Crypto Transfer");
    }

    private void setupTokenMintDTO() {
        tokenMintDTO = new TokenMintDTO();
        tokenMintDTO.setAccountId(100L);
        tokenMintDTO.setAmount(new BigDecimal("1000.0"));
        tokenMintDTO.setCryptoAssetId(cryptoAssetId);
        tokenMintDTO.setBlockchainNetworkId(blockchainNetworkId);
        tokenMintDTO.setDescription("Test Token Mint");
    }

    private void setupTokenBurnDTO() {
        tokenBurnDTO = new TokenBurnDTO();
        tokenBurnDTO.setAccountId(100L);
        tokenBurnDTO.setAmount(new BigDecimal("500.0"));
        tokenBurnDTO.setCryptoAssetId(cryptoAssetId);
        tokenBurnDTO.setBlockchainNetworkId(blockchainNetworkId);
        tokenBurnDTO.setDescription("Test Token Burn");
    }

    private void setupNftTransferDTO() {
        nftTransferDTO = new NftTransferDTO();
        nftTransferDTO.setAccountId(100L); // Source account
        nftTransferDTO.setCryptoAssetId(cryptoAssetId);
        nftTransferDTO.setBlockchainNetworkId(blockchainNetworkId);
        nftTransferDTO.setTokenId("123456");
        nftTransferDTO.setRecipientAddress(destinationAddress); // Destination address
        nftTransferDTO.setDescription("Test NFT Transfer");
    }

    @Test
    void createCryptoDeposit_Success() {
        // Arrange
        transaction.setTransactionType(TransactionTypeEnum.CRYPTO_DEPOSIT);
        transactionDTO.setTransactionType(TransactionTypeEnum.CRYPTO_DEPOSIT);

        // Remove unnecessary stubbing for mapper.toEntity
        when(repository.save(any(Transaction.class))).thenReturn(Mono.just(transaction));
        when(statusHistoryRepository.save(any(TransactionStatusHistory.class))).thenReturn(Mono.just(statusHistory));
        when(eventOutboxService.publishEvent(anyString(), anyString(), anyString(), any())).thenReturn(Mono.empty());
        when(mapper.toDTO(any(Transaction.class))).thenReturn(transactionDTO);

        // Act & Assert
        StepVerifier.create(service.createCryptoDeposit(cryptoDepositDTO))
                .expectNext(transactionDTO)
                .verifyComplete();

        verify(repository).save(any(Transaction.class));
        verify(statusHistoryRepository).save(any(TransactionStatusHistory.class));
        verify(eventOutboxService).publishEvent(anyString(), anyString(), anyString(), any());
    }

    @Test
    void createCryptoWithdrawal_Success() {
        // Arrange
        transaction.setTransactionType(TransactionTypeEnum.CRYPTO_WITHDRAWAL);
        transactionDTO.setTransactionType(TransactionTypeEnum.CRYPTO_WITHDRAWAL);

        // Remove unnecessary stubbing for mapper.toEntity
        when(repository.save(any(Transaction.class))).thenReturn(Mono.just(transaction));
        when(statusHistoryRepository.save(any(TransactionStatusHistory.class))).thenReturn(Mono.just(statusHistory));
        when(eventOutboxService.publishEvent(anyString(), anyString(), anyString(), any())).thenReturn(Mono.empty());
        when(mapper.toDTO(any(Transaction.class))).thenReturn(transactionDTO);
        when(walletService.validateAddress(anyString(), anyLong())).thenReturn(Mono.just(true));
        when(blockchainService.sendTransaction(anyString(), anyString(), any(BigDecimal.class), anyLong()))
                .thenReturn(Mono.just(blockchainTransactionHash));

        // Act & Assert
        StepVerifier.create(service.createCryptoWithdrawal(cryptoWithdrawalDTO))
                .expectNext(transactionDTO)
                .verifyComplete();

        verify(walletService).validateAddress(eq(destinationAddress), eq(cryptoAssetId));
        verify(blockchainService).sendTransaction(anyString(), eq(destinationAddress), any(BigDecimal.class), eq(cryptoAssetId));
        // Verify repository.save is called twice - once for initial save and once after updating blockchainTransactionHash
        verify(repository, times(2)).save(any(Transaction.class));
        verify(statusHistoryRepository).save(any(TransactionStatusHistory.class));
        verify(eventOutboxService).publishEvent(anyString(), anyString(), anyString(), any());
    }

    @Test
    void createCryptoTransfer_Success() {
        // Arrange
        transaction.setTransactionType(TransactionTypeEnum.CRYPTO_TRANSFER);
        transactionDTO.setTransactionType(TransactionTypeEnum.CRYPTO_TRANSFER);

        // Remove unnecessary stubbing for mapper.toEntity
        when(repository.save(any(Transaction.class))).thenReturn(Mono.just(transaction));
        when(statusHistoryRepository.save(any(TransactionStatusHistory.class))).thenReturn(Mono.just(statusHistory));
        when(eventOutboxService.publishEvent(anyString(), anyString(), anyString(), any())).thenReturn(Mono.empty());
        when(mapper.toDTO(any(Transaction.class))).thenReturn(transactionDTO);
        when(walletService.generateAddress(anyLong(), anyLong())).thenReturn(Mono.just(destinationAddress));
        // Add mock for blockchainService.sendTransaction since we're using it in the implementation
        when(blockchainService.sendTransaction(anyString(), anyString(), any(BigDecimal.class), anyLong()))
                .thenReturn(Mono.just(blockchainTransactionHash));

        // Act & Assert
        StepVerifier.create(service.createCryptoTransfer(cryptoTransferDTO))
                .expectNext(transactionDTO)
                .verifyComplete();

        verify(walletService).generateAddress(eq(cryptoTransferDTO.getDestinationAccountId()), eq(cryptoAssetId));
        // Verify repository.save is called twice - once for initial save and once after updating blockchainTransactionHash
        verify(repository, times(2)).save(any(Transaction.class));
        verify(statusHistoryRepository).save(any(TransactionStatusHistory.class));
        verify(eventOutboxService).publishEvent(anyString(), anyString(), anyString(), any());
    }

    @Test
    void createTokenMint_Success() {
        // Arrange
        transaction.setTransactionType(TransactionTypeEnum.TOKEN_MINT);
        transactionDTO.setTransactionType(TransactionTypeEnum.TOKEN_MINT);

        // Remove unnecessary stubbing for mapper.toEntity
        when(repository.save(any(Transaction.class))).thenReturn(Mono.just(transaction));
        when(statusHistoryRepository.save(any(TransactionStatusHistory.class))).thenReturn(Mono.just(statusHistory));
        when(eventOutboxService.publishEvent(anyString(), anyString(), anyString(), any())).thenReturn(Mono.empty());
        when(mapper.toDTO(any(Transaction.class))).thenReturn(transactionDTO);

        // Act & Assert
        StepVerifier.create(service.createTokenMint(tokenMintDTO))
                .expectNext(transactionDTO)
                .verifyComplete();

        verify(repository).save(any(Transaction.class));
        verify(statusHistoryRepository).save(any(TransactionStatusHistory.class));
        verify(eventOutboxService).publishEvent(anyString(), anyString(), anyString(), any());
    }

    @Test
    void createTokenBurn_Success() {
        // Arrange
        transaction.setTransactionType(TransactionTypeEnum.TOKEN_BURN);
        transactionDTO.setTransactionType(TransactionTypeEnum.TOKEN_BURN);

        // Remove unnecessary stubbing for mapper.toEntity
        when(repository.save(any(Transaction.class))).thenReturn(Mono.just(transaction));
        when(statusHistoryRepository.save(any(TransactionStatusHistory.class))).thenReturn(Mono.just(statusHistory));
        when(eventOutboxService.publishEvent(anyString(), anyString(), anyString(), any())).thenReturn(Mono.empty());
        when(mapper.toDTO(any(Transaction.class))).thenReturn(transactionDTO);

        // Act & Assert
        StepVerifier.create(service.createTokenBurn(tokenBurnDTO))
                .expectNext(transactionDTO)
                .verifyComplete();

        verify(repository).save(any(Transaction.class));
        verify(statusHistoryRepository).save(any(TransactionStatusHistory.class));
        verify(eventOutboxService).publishEvent(anyString(), anyString(), anyString(), any());
    }

    @Test
    void createNftTransfer_Success() {
        // Arrange
        transaction.setTransactionType(TransactionTypeEnum.NFT_TRANSFER);
        transactionDTO.setTransactionType(TransactionTypeEnum.NFT_TRANSFER);

        // Remove unnecessary stubbing for mapper.toEntity
        when(repository.save(any(Transaction.class))).thenReturn(Mono.just(transaction));
        when(statusHistoryRepository.save(any(TransactionStatusHistory.class))).thenReturn(Mono.just(statusHistory));
        when(eventOutboxService.publishEvent(anyString(), anyString(), anyString(), any())).thenReturn(Mono.empty());
        when(mapper.toDTO(any(Transaction.class))).thenReturn(transactionDTO);

        // Act & Assert
        StepVerifier.create(service.createNftTransfer(nftTransferDTO))
                .expectNext(transactionDTO)
                .verifyComplete();

        verify(repository).save(any(Transaction.class));
        verify(statusHistoryRepository).save(any(TransactionStatusHistory.class));
        verify(eventOutboxService).publishEvent(anyString(), anyString(), anyString(), any());
    }

    // Note: Tests for getTransactionsByBlockchainHash and getTransactionsByCryptoAssetId
    // are not included because the repository methods are not yet implemented.
    // These tests should be added once the repository methods are available.

    @Test
    void updateBlockchainTransactionHash_Success() {
        // Arrange
        when(repository.findById(transactionId)).thenReturn(Mono.just(transaction));
        when(repository.save(transaction)).thenReturn(Mono.just(transaction));
        when(mapper.toDTO(transaction)).thenReturn(transactionDTO);

        // Act & Assert
        StepVerifier.create(service.updateBlockchainTransactionHash(transactionId, blockchainTransactionHash))
                .expectNext(transactionDTO)
                .verifyComplete();

        verify(repository).findById(transactionId);
        verify(repository).save(transaction);
        verify(mapper).toDTO(transaction);
    }

    @Test
    void updateConfirmationCount_Success() {
        // Arrange
        Integer confirmationCount = 6;
        when(repository.findById(transactionId)).thenReturn(Mono.just(transaction));
        when(repository.save(transaction)).thenReturn(Mono.just(transaction));
        when(mapper.toDTO(transaction)).thenReturn(transactionDTO);

        // Act & Assert
        StepVerifier.create(service.updateConfirmationCount(transactionId, confirmationCount))
                .expectNext(transactionDTO)
                .verifyComplete();

        verify(repository).findById(transactionId);
        verify(repository).save(transaction);
        verify(mapper).toDTO(transaction);
    }
}