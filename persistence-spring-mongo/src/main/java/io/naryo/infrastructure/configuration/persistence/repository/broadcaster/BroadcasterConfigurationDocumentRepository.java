package io.naryo.infrastructure.configuration.persistence.repository.broadcaster;

import io.naryo.infrastructure.configuration.persistence.document.broadcaster.configuration.BroadcasterConfigurationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BroadcasterConfigurationDocumentRepository extends MongoRepository<BroadcasterConfigurationDocument, String> {
}
