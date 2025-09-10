package io.naryo.infrastructure.store.event.persistence.repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import io.naryo.infrastructure.store.event.persistence.document.block.BlockEventDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlockEventDocumentRepository extends MongoRepository<BlockEventDocument, String> {

    Optional<BlockEventDocument> findFirstByOrderByNumberDesc();

    Optional<BlockEventDocument> findByNumber(BigInteger number);

    List<BlockEventDocument> findAllByNumberIn(List<BigInteger> numbers);
}
