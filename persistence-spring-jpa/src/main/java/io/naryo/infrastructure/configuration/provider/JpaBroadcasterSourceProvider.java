package io.naryo.infrastructure.configuration.provider;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import io.naryo.application.configuration.source.model.broadcaster.BroadcasterDescriptor;
import io.naryo.application.configuration.source.provider.broadcaster.BroadcasterSourceProvider;
import io.naryo.infrastructure.configuration.persistence.entity.broadcaster.target.BroadcasterEntity;
import io.naryo.infrastructure.configuration.persistence.repository.broadcaster.BroadcasterEntityRepository;
import org.springframework.stereotype.Component;

@Component
public class JpaBroadcasterSourceProvider implements BroadcasterSourceProvider {

    private final BroadcasterEntityRepository repository;

    public JpaBroadcasterSourceProvider(BroadcasterEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public Collection<BroadcasterDescriptor> load() {
        List<BroadcasterEntity> broadcasterEntities = this.repository.findAll();
        return new HashSet<>(broadcasterEntities);
    }

    @Override
    public int priority() {
        return 0;
    }
}
