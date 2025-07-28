package io.naryo.infrastructure.configuration.source.env.model.event.store.block;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.model.event.BlockEventStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.event.EventStoreTargetDescriptor;
import io.naryo.domain.configuration.eventstore.EventStoreStrategy;
import io.naryo.domain.configuration.eventstore.EventStoreType;
import io.naryo.infrastructure.configuration.source.env.model.event.store.EventStoreConfigurationProperties;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public abstract class BlockEventStoreConfigurationProperties
        extends EventStoreConfigurationProperties
        implements BlockEventStoreConfigurationDescriptor {

    private Set<EventStoreTargetProperties> targets;

    public BlockEventStoreConfigurationProperties(
            @NotNull UUID nodeId,
            @Nullable EventStoreType type,
            Set<EventStoreTargetProperties> targets,
            @Nullable Map<String, Object> additionalProperties,
            @Nullable ConfigurationSchema propertiesSchema) {
        super(nodeId, type, EventStoreStrategy.BLOCK_BASED, additionalProperties, propertiesSchema);
        this.targets = new HashSet<>(targets);
    }

    @Override
    public void setTargets(Set<? extends EventStoreTargetDescriptor> targets) {
        if (targets.stream().allMatch(t -> t instanceof EventStoreTargetProperties)) {
            this.targets =
                    targets.stream()
                            .map(t -> (EventStoreTargetProperties) t)
                            .collect(Collectors.toSet());
        } else {
            throw new IllegalArgumentException("Unsupported target type: " + targets.getClass());
        }
    }
}
