package io.naryo.infrastructure.provider.broadcaster;

import io.naryo.application.broadcaster.configuration.mapper.BroadcasterConfigurationMapperRegistry;
import io.naryo.application.broadcaster.configuration.provider.BroadcasterConfigurationConfigurationProvider;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.infrastructure.persistance.document.broadcaster.BroadcasterConfigurationDocument;
import io.naryo.infrastructure.persistance.repository.broadcaster.BroadcasterConfigurationDocumentRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class MongoBroadcasterConfigurationConfigurationProvider
    implements BroadcasterConfigurationConfigurationProvider {

    private final BroadcasterConfigurationDocumentRepository repository;
    private final BroadcasterConfigurationMapperRegistry registry;

    public MongoBroadcasterConfigurationConfigurationProvider(
        BroadcasterConfigurationDocumentRepository repository,
        BroadcasterConfigurationMapperRegistry registry) {
        this.repository = repository;
        this.registry = registry;
    }

    @Override
    public Collection<BroadcasterConfiguration> load() {
        return this.repository.findAll().stream().map(this::toBroadcasterConfiguration)
            .collect(Collectors.toSet());
    }

    @Override
    public int priority() {
        return 1;
    }

    private BroadcasterConfiguration toBroadcasterConfiguration(
        BroadcasterConfigurationDocument props) {
        return registry.map(props.getType(), props);
    }
}
