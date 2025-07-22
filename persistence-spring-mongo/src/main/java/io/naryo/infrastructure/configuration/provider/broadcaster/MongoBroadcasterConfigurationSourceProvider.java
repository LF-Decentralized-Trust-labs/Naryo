package io.naryo.infrastructure.configuration.provider.broadcaster;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterConfigurationDescriptor;
import io.naryo.application.configuration.source.provider.broadcaster.BroadcasterConfigurationSourceProvider;
import io.naryo.infrastructure.configuration.persistence.document.broadcaster.configuration.BroadcasterConfigurationDocument;
import io.naryo.infrastructure.configuration.persistence.repository.broadcaster.BroadcasterConfigurationDocumentRepository;
import org.springframework.stereotype.Component;

@Component
public class MongoBroadcasterConfigurationSourceProvider
        implements BroadcasterConfigurationSourceProvider {

    private final BroadcasterConfigurationDocumentRepository repository;

    public MongoBroadcasterConfigurationSourceProvider(
            BroadcasterConfigurationDocumentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Collection<BroadcasterConfigurationDescriptor> load() {
        List<BroadcasterConfigurationDocument> broadcasterConfigurations =
                this.repository.findAll();
        return new HashSet<>(broadcasterConfigurations);
    }

    @Override
    public int priority() {
        return 1;
    }
}
