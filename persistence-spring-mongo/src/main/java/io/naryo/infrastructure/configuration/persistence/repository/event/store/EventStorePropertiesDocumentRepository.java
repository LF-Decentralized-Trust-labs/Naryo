package io.naryo.infrastructure.configuration.persistence.repository.event.store;

import io.naryo.infrastructure.configuration.persistence.document.store.StoreConfigurationPropertiesDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventStorePropertiesDocumentRepository
        extends MongoRepository<StoreConfigurationPropertiesDocument, String> {}
