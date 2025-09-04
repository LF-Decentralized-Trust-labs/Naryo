package io.naryo.infrastructure.configuration.persistence.document.store.event.block;

import java.util.HashSet;
import java.util.Set;

import io.naryo.application.configuration.source.model.store.event.BlockEventStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.store.event.EventStoreTargetDescriptor;
import io.naryo.infrastructure.configuration.persistence.document.store.event.EventStoreConfigurationPropertiesDocument;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document
@TypeAlias("block_event_store")
public final class BlockStoreConfigurationPropertiesDocument
        extends EventStoreConfigurationPropertiesDocument
        implements BlockEventStoreConfigurationDescriptor {

    private Set<EventStoreTargetPropertiesDocument> targets;

    public BlockStoreConfigurationPropertiesDocument(
            Set<EventStoreTargetPropertiesDocument> targets) {
        this.targets = targets;
    }

    @Override
    public void setTargets(Set<? extends EventStoreTargetDescriptor> targets) {
        if (targets.stream().allMatch(t -> t instanceof EventStoreTargetPropertiesDocument)) {
            this.targets =
                    targets.stream()
                            .map(t -> (EventStoreTargetPropertiesDocument) t)
                            .collect(HashSet::new, Set::add, Set::addAll);
        } else {
            throw new IllegalArgumentException("Unsupported target type: " + targets.getClass());
        }
    }
}
