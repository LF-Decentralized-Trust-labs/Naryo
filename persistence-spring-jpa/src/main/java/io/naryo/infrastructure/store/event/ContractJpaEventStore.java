package io.naryo.infrastructure.store.event;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.naryo.application.store.event.block.ContractEventEventStore;
import io.naryo.domain.configuration.store.active.JpaActiveStoreConfiguration;
import io.naryo.domain.event.contract.ContractEvent;
import io.naryo.infrastructure.store.event.persistence.entity.contract.ContractEventEntity;
import io.naryo.infrastructure.store.event.persistence.repository.ContractEventEntityRepository;
import org.springframework.stereotype.Component;

@Component
public final class ContractJpaEventStore extends JpaEventStore<String, ContractEvent>
        implements ContractEventEventStore<JpaActiveStoreConfiguration> {

    private final ContractEventEntityRepository contractEventEntityRepository;

    public ContractJpaEventStore(ContractEventEntityRepository contractEventEntityRepository) {
        this.contractEventEntityRepository = contractEventEntityRepository;
    }

    @Override
    protected Class<?> getTargetDataClass() {
        return ContractEvent.class;
    }

    @Override
    public void save(JpaActiveStoreConfiguration configuration, String key, ContractEvent data) {
        contractEventEntityRepository.save(ContractEventEntity.fromContractEvent(data));
    }

    @Override
    public Optional<ContractEvent> get(JpaActiveStoreConfiguration configuration, String key) {
        String[] parts = key.split(":", 2);

        String blockHash = parts[0];
        BigInteger logIndex = new BigInteger(parts[1]);

        return contractEventEntityRepository
                .findByBlockHashAndLogIndex(blockHash, logIndex)
                .map(ContractEventEntity::toContractEvent);
    }

    @Override
    public List<ContractEvent> get(JpaActiveStoreConfiguration configuration, List<String> keys) {
        List<ContractEvent> result = new ArrayList<>();

        keys.forEach(
                key -> {
                    String[] parts = key.split(":", 2);
                    String blockHash = parts[0];
                    BigInteger logIndex = new BigInteger(parts[1]);

                    contractEventEntityRepository
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
