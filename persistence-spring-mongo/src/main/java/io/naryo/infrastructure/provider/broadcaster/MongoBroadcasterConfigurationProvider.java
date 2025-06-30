package io.naryo.infrastructure.provider.broadcaster;

import io.naryo.application.broadcaster.configuration.provider.BroadcasterConfigurationProvider;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.broadcaster.BroadcasterTargetType;
import io.naryo.domain.broadcaster.Destination;
import io.naryo.domain.broadcaster.target.*;
import io.naryo.infrastructure.persistance.document.broadcaster.BroadcasterDocument;
import io.naryo.infrastructure.persistance.document.broadcaster.BroadcasterTargetDocument;
import io.naryo.infrastructure.persistance.repository.broadcaster.BroadcasterDocumentRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public final class MongoBroadcasterConfigurationProvider implements BroadcasterConfigurationProvider {

    private final BroadcasterDocumentRepository repository;

    public MongoBroadcasterConfigurationProvider(BroadcasterDocumentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Collection<Broadcaster> load() {
        return this.repository.findAll().stream()
                .map(this::toBroadcaster)
                .collect(Collectors.toSet());
    }

    @Override
    public int priority() {
        return 1;
    }

    private Broadcaster toBroadcaster(BroadcasterDocument props) {
        BroadcasterTargetDocument target = props.getTarget();
        Destination destination = new Destination(target.getDestination());
        return new Broadcaster(
                props.getId(),
                switch (target.getType()) {
                    case BroadcasterTargetType.BLOCK -> new BlockBroadcasterTarget(destination);
                    case BroadcasterTargetType.TRANSACTION -> new TransactionBroadcasterTarget(destination);
                    case BroadcasterTargetType.CONTRACT_EVENT -> new ContractEventBroadcasterTarget(destination);
                    case BroadcasterTargetType.FILTER -> new FilterEventBroadcasterTarget(destination, target.getFilterId());
                    case BroadcasterTargetType.ALL -> new AllBroadcasterTarget(destination);
                },
                props.getConfigurationId());
    }
}
