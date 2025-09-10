package io.naryo.infrastructure.event.mongo.event;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.naryo.application.store.event.block.ContractEventEventStore;
import io.naryo.domain.configuration.eventstore.active.block.MongoStoreConfiguration;
import io.naryo.domain.event.contract.ContractEvent;
import io.naryo.infrastructure.event.mongo.event.persistence.document.ContractEventDocument;
import io.naryo.infrastructure.event.mongo.event.persistence.repository.ContractEventDocumentRepository;
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
        String[] parts = key.split(":", 2);

        String blockHash = parts[0];
        BigInteger logIndex = new BigInteger(parts[1]);

        return contractEventDocumentRepository
                .findByBlockHashAndLogIndex(blockHash, logIndex)
                .map(ContractEventDocument::toContractEvent);
    }

    @Override
    public List<ContractEvent> get(MongoStoreConfiguration configuration, List<String> keys) {
        List<ContractEvent> result = new ArrayList<>();

        keys.forEach(
                key -> {
                    String[] parts = key.split(":", 2);
                    String blockHash = parts[0];
                    BigInteger logIndex = new BigInteger(parts[1]);

                    contractEventDocumentRepository
                            .findByBlockHashAndLogIndex(blockHash, logIndex)
                            .ifPresent(
                                    entity -> {
                                        ContractEvent contractEvent = entity.toContractEvent();
                                        result.add(contractEvent);
                                    });
                });

        return result;
    }
}
