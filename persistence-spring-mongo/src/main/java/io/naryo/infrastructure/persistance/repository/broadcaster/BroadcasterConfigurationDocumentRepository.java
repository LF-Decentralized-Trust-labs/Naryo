package io.naryo.infrastructure.persistance.repository.broadcaster;

import io.naryo.infrastructure.persistance.document.broadcaster.BroadcasterConfigurationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface BroadcasterConfigurationDocumentRepository
    extends MongoRepository<BroadcasterConfigurationDocument, UUID> {
}
