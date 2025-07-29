package io.naryo.infrastructure.configuration.provider.event.store;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import io.naryo.application.configuration.source.model.event.EventStoreConfigurationDescriptor;
import io.naryo.application.event.store.configuration.provider.EventStoreSourceProvider;
import io.naryo.infrastructure.configuration.persistence.document.event.store.EventStoreConfigurationPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.repository.event.store.EventStorePropertiesDocumentRepository;
import org.springframework.stereotype.Component;

@Component
public final class MongoEventStoreSourceProvider implements EventStoreSourceProvider {

    private final EventStorePropertiesDocumentRepository repository;

    public MongoEventStoreSourceProvider(EventStorePropertiesDocumentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Collection<EventStoreConfigurationDescriptor> load() {
        List<EventStoreConfigurationPropertiesDocument> eventStores = this.repository.findAll();
        return new HashSet<>(eventStores);
    }

    @Override
    public int priority() {
        return 0;
    }
}
