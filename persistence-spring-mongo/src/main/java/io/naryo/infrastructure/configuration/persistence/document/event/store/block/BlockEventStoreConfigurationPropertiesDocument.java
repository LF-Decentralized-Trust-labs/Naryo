package io.naryo.infrastructure.configuration.persistence.document.event.store.block;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.naryo.application.configuration.source.model.event.BlockEventStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.event.EventStoreTargetDescriptor;
import io.naryo.domain.configuration.eventstore.EventStoreStrategy;
import io.naryo.domain.configuration.eventstore.EventStoreType;
import io.naryo.infrastructure.configuration.persistence.document.common.ConfigurationSchemaDocument;
import io.naryo.infrastructure.configuration.persistence.document.event.store.EventStoreConfigurationPropertiesDocument;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public abstract class BlockEventStoreConfigurationPropertiesDocument
        extends EventStoreConfigurationPropertiesDocument
        implements BlockEventStoreConfigurationDescriptor {

    private Set<EventStoreTargetPropertiesDocument> targets;

    public BlockEventStoreConfigurationPropertiesDocument(
            @NotNull String nodeId,
            @Nullable EventStoreType type,
            Set<EventStoreTargetPropertiesDocument> targets,
            Map<String, Object> additionalProperties,
            @Nullable ConfigurationSchemaDocument propertiesSchema) {
        super(nodeId, type, EventStoreStrategy.BLOCK_BASED, additionalProperties, propertiesSchema);
        this.targets = new HashSet<>(targets);
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
