package io.naryo.infrastructure.store.event.persistence.repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import io.naryo.infrastructure.store.event.persistence.document.contract.ContractEventDocument;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ContractEventDocumentRepository
        extends MongoRepository<ContractEventDocument, ObjectId> {
    Optional<ContractEventDocument> findByTransactionHashAndLogIndex(
            String transactionHash, BigInteger logIndex);

    @Query("{ 'transactionHash' : { $in : ?0 }, 'logIndex' : { $in : ?1 } }")
    List<ContractEventDocument> findByTransactionHashAndLogIndexIn(
            List<String> transactionHashes, List<BigInteger> logIndexes);
}
