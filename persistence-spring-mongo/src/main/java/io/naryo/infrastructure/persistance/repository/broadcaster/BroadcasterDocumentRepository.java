package io.naryo.infrastructure.persistance.repository.broadcaster;

import io.naryo.infrastructure.persistance.document.broadcaster.BroadcasterDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface BroadcasterDocumentRepository extends MongoRepository<BroadcasterDocument, UUID> {
}
