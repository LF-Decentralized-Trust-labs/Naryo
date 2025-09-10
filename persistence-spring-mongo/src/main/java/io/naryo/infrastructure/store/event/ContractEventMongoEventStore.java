package io.naryo.infrastructure.store.event;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.naryo.application.store.event.block.ContractEventEventStore;
import io.naryo.domain.configuration.store.MongoStoreConfiguration;
import io.naryo.domain.event.contract.ContractEvent;
import io.naryo.infrastructure.store.event.persistence.document.contract.ContractEventDocument;
import io.naryo.infrastructure.store.event.persistence.document.contract.ContractEventDocumentId;
import io.naryo.infrastructure.store.event.persistence.repository.ContractEventDocumentRepository;
import org.springframework.stereotype.Component;

@Component
public final class ContractEventMongoEventStore extends MongoEventStore<String, ContractEvent>
        implements ContractEventEventStore<MongoStoreConfiguration> {

    private final ContractEventDocumentRepository contractEventDocumentRepository;

    public ContractEventMongoEventStore(
            ContractEventDocumentRepository contractEventDocumentRepository) {
        this.contractEventDocumentRepository = contractEventDocumentRepository;
    }

    @Override
    protected Class<?> getTargetDataClass() {
        return ContractEvent.class;
    }

    @Override
    public void save(MongoStoreConfiguration configuration, String key, ContractEvent data) {
        contractEventDocumentRepository.save(ContractEventDocument.fromContractEvent(data));
    }

    @Override
    public Optional<ContractEvent> get(MongoStoreConfiguration configuration, String key) {
        ContractEventDocumentId id = getIdFromKey(key);

        return contractEventDocumentRepository
                .findById(id)
                .map(ContractEventDocument::toContractEvent);
    }

    @Override
    public List<ContractEvent> get(MongoStoreConfiguration configuration, List<String> keys) {
        List<ContractEventDocumentId> ids = keys.stream().map(this::getIdFromKey).toList();

        return contractEventDocumentRepository.findAllById(ids).stream()
                .map(ContractEventDocument::toContractEvent)
                .collect(Collectors.toList());
    }

    private ContractEventDocumentId getIdFromKey(String key) {
        String[] parts = key.split(":", 2);
        String transactionHash = parts[0];
        BigInteger logIndex = new BigInteger(parts[1]);

        return new ContractEventDocumentId(transactionHash, logIndex);
    }
}
