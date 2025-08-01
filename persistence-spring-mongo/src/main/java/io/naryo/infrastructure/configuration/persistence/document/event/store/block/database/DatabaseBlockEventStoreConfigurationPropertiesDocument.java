package io.naryo.infrastructure.configuration.persistence.document.event.store.block.database;

import java.util.Map;
import java.util.Set;

import io.naryo.application.configuration.source.model.event.DatabaseBlockEventStoreConfigurationDescriptor;
import io.naryo.domain.configuration.eventstore.EventStoreType;
import io.naryo.domain.configuration.eventstore.database.DatabaseEngine;
import io.naryo.infrastructure.configuration.persistence.document.common.ConfigurationSchemaDocument;
import io.naryo.infrastructure.configuration.persistence.document.event.store.block.BlockEventStoreConfigurationPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.event.store.block.EventStoreTargetPropertiesDocument;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

@Setter
@TypeAlias("database_event_store")
public final class DatabaseBlockEventStoreConfigurationPropertiesDocument
        extends BlockEventStoreConfigurationPropertiesDocument
        implements DatabaseBlockEventStoreConfigurationDescriptor {

    private String engine;

    public DatabaseBlockEventStoreConfigurationPropertiesDocument(
            @NotNull String nodeId,
            Set<EventStoreTargetPropertiesDocument> targets,
            Map<String, Object> additionalProperties,
            @Nullable ConfigurationSchemaDocument propertiesSchema,
            @NotNull String engine) {
        super(nodeId, EventStoreType.DATABASE, targets, additionalProperties, propertiesSchema);
        this.engine = engine;
    }

    @Override
    public DatabaseEngine getEngine() {
        return this.engine::toLowerCase;
    }
}
