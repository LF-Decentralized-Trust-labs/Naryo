package io.naryo.infrastructure.configuration.persistence.repository.filter;

import io.naryo.infrastructure.configuration.persistence.document.filter.FilterPropertiesDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface FilterDocumentRepository extends MongoRepository<FilterPropertiesDocument, UUID> {
}
