package io.naryo.infrastructure.store.event;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.naryo.application.store.event.block.TransactionEventStore;
import io.naryo.domain.configuration.store.MongoStoreConfiguration;
import io.naryo.domain.event.transaction.TransactionEvent;
import io.naryo.infrastructure.store.event.persistence.document.transaction.TransactionEventDocument;
import io.naryo.infrastructure.store.event.persistence.repository.TransactionEventDocumentRepository;
import org.springframework.stereotype.Component;

@Component
public final class TransactionMongoEventStore extends MongoEventStore<String, TransactionEvent>
        implements TransactionEventStore<MongoStoreConfiguration> {

    private final TransactionEventDocumentRepository transactionEventDocumentRepository;

    public TransactionMongoEventStore(
            TransactionEventDocumentRepository transactionEventDocumentRepository) {
        this.transactionEventDocumentRepository = transactionEventDocumentRepository;
    }

    @Override
    protected Class<?> getTargetDataClass() {
        return TransactionEvent.class;
    }

    @Override
    public void save(MongoStoreConfiguration configuration, String key, TransactionEvent data) {
        transactionEventDocumentRepository.save(
                TransactionEventDocument.fromTransactionEvent(data));
    }

    @Override
    public Optional<TransactionEvent> get(MongoStoreConfiguration configuration, String key) {
        return transactionEventDocumentRepository
                .findByHash(key)
                .map(TransactionEventDocument::toTransactionEvent);
    }

    @Override
    public List<TransactionEvent> get(MongoStoreConfiguration configuration, List<String> keys) {
        return transactionEventDocumentRepository.findAllByHashIn(keys).stream()
                .map(TransactionEventDocument::toTransactionEvent)
                .collect(Collectors.toList());
    }
}
