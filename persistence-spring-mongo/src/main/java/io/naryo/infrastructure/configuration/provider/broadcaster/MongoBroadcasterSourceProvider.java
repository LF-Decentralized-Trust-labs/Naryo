package io.naryo.infrastructure.configuration.provider.broadcaster;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import io.naryo.application.configuration.source.model.broadcaster.BroadcasterDescriptor;
import io.naryo.application.configuration.source.provider.broadcaster.BroadcasterSourceProvider;
import io.naryo.infrastructure.configuration.persistence.document.broadcaster.target.BroadcasterDocument;
import io.naryo.infrastructure.configuration.persistence.repository.broadcaster.BroadcasterDocumentRepository;
import org.springframework.stereotype.Component;

@Component
public class MongoBroadcasterSourceProvider implements BroadcasterSourceProvider {

    private final BroadcasterDocumentRepository repository;

    public MongoBroadcasterSourceProvider(BroadcasterDocumentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Collection<BroadcasterDescriptor> load() {
        List<BroadcasterDocument> broadcasters = this.repository.findAll();
        return new HashSet<>(broadcasters);
    }

    @Override
    public int priority() {
        return 1;
    }
}
