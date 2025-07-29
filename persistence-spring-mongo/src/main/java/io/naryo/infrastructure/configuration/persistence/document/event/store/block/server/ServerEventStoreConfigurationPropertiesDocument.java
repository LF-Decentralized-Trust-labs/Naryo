package io.naryo.infrastructure.configuration.persistence.document.event.store.block.server;

import java.util.Map;
import java.util.Set;

import io.naryo.application.configuration.source.model.event.ServerBlockEventStoreConfigurationDescriptor;
import io.naryo.domain.configuration.eventstore.EventStoreType;
import io.naryo.domain.configuration.eventstore.server.ServerType;
import io.naryo.infrastructure.configuration.persistence.document.common.ConfigurationSchemaDocument;
import io.naryo.infrastructure.configuration.persistence.document.event.store.block.BlockEventStoreConfigurationPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.event.store.block.EventStoreTargetPropertiesDocument;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

@Setter
@TypeAlias("server_event_store")
public final class ServerEventStoreConfigurationPropertiesDocument
        extends BlockEventStoreConfigurationPropertiesDocument
        implements ServerBlockEventStoreConfigurationDescriptor {

    private @NotNull String serverType;

    public ServerEventStoreConfigurationPropertiesDocument(
            @NotNull String nodeId,
            Set<EventStoreTargetPropertiesDocument> targets,
            Map<String, Object> additionalProperties,
            @Nullable ConfigurationSchemaDocument propertiesSchema,
            @NotNull String serverType) {
        super(nodeId, EventStoreType.SERVER, targets, additionalProperties, propertiesSchema);
        this.serverType = serverType;
    }

    @Override
    public ServerType getServerType() {
        return () -> serverType.toLowerCase();
    }
}
