package io.naryo.infrastructure.store.event;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.naryo.application.store.event.block.TransactionEventStore;
import io.naryo.domain.configuration.store.active.JpaActiveStoreConfiguration;
import io.naryo.domain.event.transaction.TransactionEvent;
import io.naryo.infrastructure.store.event.persistence.entity.transaction.TransactionEventEntity;
import io.naryo.infrastructure.store.event.persistence.repository.TransactionEventEntityRepository;
import org.springframework.stereotype.Component;

@Component
public final class TransactionJpaEventStore extends JpaEventStore<String, TransactionEvent>
        implements TransactionEventStore<JpaActiveStoreConfiguration> {

    private final TransactionEventEntityRepository transactionEventEntityRepository;

    public TransactionJpaEventStore(
            TransactionEventEntityRepository transactionEventEntityRepository) {
        this.transactionEventEntityRepository = transactionEventEntityRepository;
    }

    @Override
    protected Class<?> getTargetDataClass() {
        return TransactionEvent.class;
    }

    @Override
    public void save(JpaActiveStoreConfiguration configuration, String key, TransactionEvent data) {
        transactionEventEntityRepository.save(TransactionEventEntity.fromTransactionEvent(data));
    }

    @Override
    public Optional<TransactionEvent> get(JpaActiveStoreConfiguration configuration, String key) {
        return transactionEventEntityRepository
                .findByHash(key)
                .map(TransactionEventEntity::toTransactionEvent);
    }

    @Override
    public List<TransactionEvent> get(
            JpaActiveStoreConfiguration configuration, List<String> keys) {
        return transactionEventEntityRepository.findAllByHashIn(keys).stream()
                .map(TransactionEventEntity::toTransactionEvent)
                .collect(Collectors.toList());
    }
}
