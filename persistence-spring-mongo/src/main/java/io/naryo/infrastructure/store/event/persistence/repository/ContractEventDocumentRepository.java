package io.naryo.infrastructure.store.event.persistence.repository;

import io.naryo.infrastructure.store.event.persistence.document.contract.ContractEventDocument;
import io.naryo.infrastructure.store.event.persistence.document.contract.ContractEventDocumentId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContractEventDocumentRepository
        extends MongoRepository<ContractEventDocument, ContractEventDocumentId> {
}
