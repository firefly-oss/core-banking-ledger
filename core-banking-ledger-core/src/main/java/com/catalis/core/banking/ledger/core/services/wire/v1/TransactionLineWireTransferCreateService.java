package com.catalis.core.banking.ledger.core.services.wire.v1;

import com.catalis.core.banking.ledger.core.mappers.wire.v1.TransactionLineWireTransferMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.wire.v1.TransactionLineWireTransferDTO;
import com.catalis.core.banking.ledger.models.repositories.wire.v1.TransactionLineWireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TransactionLineWireTransferCreateService {

    @Autowired
    private TransactionLineWireRepository repository;

    @Autowired
    private TransactionLineWireTransferMapper mapper;

    /**
     * Creates a transaction line for a wire transfer.
     * The method takes a TransactionLineWireTransferDTO object, maps it to an entity,
     * saves the entity to the database and then maps it back to a DTO.
     *
     * @param dto the TransactionLineWireTransferDTO containing details of the wire transfer transaction line to be created
     * @return a Mono emitting the newly created TransactionLineWireTransferDTO
     */
    public Mono<TransactionLineWireTransferDTO> createTransactionLineWireTransfer(TransactionLineWireTransferDTO dto) {
        return Mono.just(dto)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }


}