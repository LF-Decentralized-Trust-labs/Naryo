package io.naryo.infrastructure.configuration.persistence.repository.node;

import io.naryo.infrastructure.configuration.persistence.document.node.NodePropertiesDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NodePropertiesDocumentRepository
        extends MongoRepository<NodePropertiesDocument, String> {}
