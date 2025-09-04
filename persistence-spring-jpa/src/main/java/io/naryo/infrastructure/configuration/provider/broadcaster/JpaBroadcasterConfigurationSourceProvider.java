package io.naryo.infrastructure.configuration.provider.broadcaster;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterConfigurationDescriptor;
import io.naryo.application.configuration.source.provider.broadcaster.BroadcasterConfigurationSourceProvider;
import io.naryo.infrastructure.configuration.persistence.entity.broadcaster.configuration.BroadcasterConfigurationEntity;
import io.naryo.infrastructure.configuration.persistence.repository.broadcaster.BroadcasterConfigurationEntityRepository;
import org.springframework.stereotype.Component;

@Component
public class JpaBroadcasterConfigurationSourceProvider
        implements BroadcasterConfigurationSourceProvider {

    private final BroadcasterConfigurationEntityRepository repository;

    public JpaBroadcasterConfigurationSourceProvider(
            BroadcasterConfigurationEntityRepository broadcasterConfigurationRepository) {
        this.repository = broadcasterConfigurationRepository;
    }

    @Override
    public Collection<BroadcasterConfigurationDescriptor> load() {
        List<BroadcasterConfigurationEntity> broadcasterConfigurationEntities =
                this.repository.findAll();
        return new HashSet<>(broadcasterConfigurationEntities);
    }

    @Override
    public int priority() {
        return 0;
    }
}
