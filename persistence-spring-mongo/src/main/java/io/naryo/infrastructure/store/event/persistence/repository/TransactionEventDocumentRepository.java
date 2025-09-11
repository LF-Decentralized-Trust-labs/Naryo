package io.naryo.infrastructure.store.event.persistence.repository;

import java.util.List;
import java.util.Optional;

import io.naryo.infrastructure.store.event.persistence.document.transaction.TransactionEventDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionEventDocumentRepository
        extends MongoRepository<TransactionEventDocument, String> {

    Optional<TransactionEventDocument> findByHash(String hash);

    List<TransactionEventDocument> findAllByHashIn(List<String> hashes);
}
