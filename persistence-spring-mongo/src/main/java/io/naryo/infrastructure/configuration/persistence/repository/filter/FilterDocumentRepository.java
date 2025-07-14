package io.naryo.infrastructure.configuration.persistence.repository.filter;

import io.naryo.infrastructure.configuration.persistence.document.filter.FilterPropertiesDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FilterDocumentRepository extends MongoRepository<FilterPropertiesDocument, String> {
}
