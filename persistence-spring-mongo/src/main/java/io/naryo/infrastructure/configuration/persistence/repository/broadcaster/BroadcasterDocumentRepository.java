package io.naryo.infrastructure.configuration.persistence.repository.broadcaster;

import io.naryo.infrastructure.configuration.persistence.document.broadcaster.target.BroadcasterDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BroadcasterDocumentRepository
        extends MongoRepository<BroadcasterDocument, String> {}
