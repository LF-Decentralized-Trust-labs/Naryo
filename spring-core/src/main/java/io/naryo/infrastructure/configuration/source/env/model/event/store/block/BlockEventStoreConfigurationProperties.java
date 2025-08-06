package io.naryo.infrastructure.configuration.source.env.model.event.store.block;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.model.event.BlockEventStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.event.EventStoreTargetDescriptor;
import io.naryo.domain.configuration.store.active.StoreType;
import io.naryo.domain.configuration.store.active.feature.event.EventStoreStrategy;
import io.naryo.infrastructure.configuration.source.env.model.event.store.ActiveEventStoreConfigurationProperties;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class BlockEventStoreConfigurationProperties extends ActiveEventStoreConfigurationProperties
        implements BlockEventStoreConfigurationDescriptor {

    private Set<EventStoreTargetProperties> targets;

    public BlockEventStoreConfigurationProperties(
            @NotNull UUID nodeId,
            @Nullable StoreType type,
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
