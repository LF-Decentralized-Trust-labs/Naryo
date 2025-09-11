package io.naryo.infrastructure.store.filter.persistence.repository;

import java.util.List;

import io.naryo.infrastructure.store.filter.persistence.document.FilterStateDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FilterStateDocumentRepository
        extends MongoRepository<FilterStateDocument, String> {

    List<FilterStateDocument> findAllByFilterIdIn(List<String> ids);
}
