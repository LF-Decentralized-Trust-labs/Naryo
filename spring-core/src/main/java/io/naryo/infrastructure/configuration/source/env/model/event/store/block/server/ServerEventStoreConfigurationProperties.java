package io.naryo.infrastructure.configuration.source.env.model.event.store.block.server;

import java.util.Map;
import java.util.Set;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.model.event.ServerBlockEventStoreConfigurationDescriptor;
import io.naryo.domain.configuration.eventstore.EventStoreType;
import io.naryo.domain.configuration.eventstore.server.ServerType;
import io.naryo.infrastructure.configuration.source.env.model.event.store.block.BlockEventStoreConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.event.store.block.EventStoreTargetProperties;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;

@Setter
public final class ServerEventStoreConfigurationProperties
        extends BlockEventStoreConfigurationProperties
        implements ServerBlockEventStoreConfigurationDescriptor {

    private @NotNull ServerType serverType;

    public ServerEventStoreConfigurationProperties(
            Set<EventStoreTargetProperties> targets,
            @Nullable Map<String, Object> additionalProperties,
            @Nullable ConfigurationSchema propertiesSchema,
            @NotNull ServerType serverType) {
        super(EventStoreType.SERVER, targets, additionalProperties, propertiesSchema);
        this.serverType = serverType;
    }

    @Override
    public ServerType getServerType() {
        return serverType;
    }
}
