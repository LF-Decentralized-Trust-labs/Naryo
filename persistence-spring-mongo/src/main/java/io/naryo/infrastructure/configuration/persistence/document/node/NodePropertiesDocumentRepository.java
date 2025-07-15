package io.naryo.infrastructure.configuration.persistence.document.node;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface NodePropertiesDocumentRepository extends MongoRepository<NodePropertiesDocument, String> {
}
