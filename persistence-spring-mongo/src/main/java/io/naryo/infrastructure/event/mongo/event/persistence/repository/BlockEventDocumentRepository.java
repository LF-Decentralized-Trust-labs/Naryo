package io.naryo.infrastructure.event.mongo.event.persistence.repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import io.naryo.infrastructure.event.mongo.event.persistence.document.BlockEventDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlockEventDocumentRepository extends MongoRepository<BlockEventDocument, String> {

    Optional<BlockEventDocument> findFirstByOrderByNumberDesc();

    Optional<BlockEventDocument> findByNumber(BigInteger number);

    List<BlockEventDocument> findAllByNumberIn(List<BigInteger> numbers);
}
