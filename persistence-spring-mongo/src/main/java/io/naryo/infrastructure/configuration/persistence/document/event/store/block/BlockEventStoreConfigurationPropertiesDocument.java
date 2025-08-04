package io.naryo.infrastructure.configuration.persistence.document.event.store.block;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.naryo.application.configuration.source.model.event.BlockEventStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.event.EventStoreTargetDescriptor;
import io.naryo.domain.configuration.eventstore.active.EventStoreStrategy;
import io.naryo.domain.configuration.eventstore.active.EventStoreType;
import io.naryo.infrastructure.configuration.persistence.document.common.ConfigurationSchemaDocument;
import io.naryo.infrastructure.configuration.persistence.document.event.store.ActiveEventStoreConfigurationPropertiesDocument;
import jakarta.annotation.Nullable;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@Getter
@TypeAlias("block_event_store")
public final class BlockEventStoreConfigurationPropertiesDocument
        extends ActiveEventStoreConfigurationPropertiesDocument
        implements BlockEventStoreConfigurationDescriptor {

    private Set<EventStoreTargetPropertiesDocument> targets;

    public BlockEventStoreConfigurationPropertiesDocument(
            String nodeId,
            EventStoreType type,
            Map<String, Object> additionalProperties,
            @Nullable ConfigurationSchemaDocument propertiesSchema,
            Set<EventStoreTargetPropertiesDocument> targets) {
        super(nodeId, type, EventStoreStrategy.BLOCK_BASED, additionalProperties, propertiesSchema);
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
