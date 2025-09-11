package io.naryo.infrastructure.configuration.persistence.repository.store;

import io.naryo.infrastructure.configuration.persistence.document.store.StoreConfigurationPropertiesDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StorePropertiesDocumentRepository
        extends MongoRepository<StoreConfigurationPropertiesDocument, String> {}
