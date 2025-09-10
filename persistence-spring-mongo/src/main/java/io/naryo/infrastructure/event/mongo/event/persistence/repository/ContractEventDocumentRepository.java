package io.naryo.infrastructure.event.mongo.event.persistence.repository;

import java.math.BigInteger;
import java.util.Optional;

import io.naryo.infrastructure.event.mongo.event.persistence.document.ContractEventDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContractEventDocumentRepository
        extends MongoRepository<ContractEventDocument, String> {
    Optional<ContractEventDocument> findByBlockHashAndLogIndex(
            String blockHash, BigInteger logIndex);
}
