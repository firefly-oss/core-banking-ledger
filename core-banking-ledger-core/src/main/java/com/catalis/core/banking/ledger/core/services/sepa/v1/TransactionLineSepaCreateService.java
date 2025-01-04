package com.catalis.core.banking.ledger.core.services.sepa.v1;


import com.catalis.core.banking.ledger.core.mappers.sepa.v1.TransactionLineSepaTransferMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.sepa.v1.TransactionLineSepaTransferDTO;
import com.catalis.core.banking.ledger.models.repositories.sepa.v1.TransactionLineSepaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TransactionLineSepaCreateService {

    @Autowired
    private TransactionLineSepaRepository repository;

    @Autowired
    private TransactionLineSepaTransferMapper mapper;

    /**
     * Creates a new SEPA transfer transaction line.
     *
     * @param transactionLineSepaTransferDTO the data transfer object containing the details of the SEPA transfer transaction line to be created
     * @return a {@code Mono} containing the created TransactionLineSepaTransferDTO
     */
    public Mono<TransactionLineSepaTransferDTO> createTransactionLineSepaTransfer(
            TransactionLineSepaTransferDTO transactionLineSepaTransferDTO) {
        return Mono.just(transactionLineSepaTransferDTO)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

}