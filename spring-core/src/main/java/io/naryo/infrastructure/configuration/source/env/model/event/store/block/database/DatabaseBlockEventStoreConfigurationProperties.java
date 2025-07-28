package io.naryo.infrastructure.configuration.source.env.model.event.store.block.database;

import java.util.Map;
import java.util.Set;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.model.event.DatabaseBlockEventStoreConfigurationDescriptor;
import io.naryo.domain.configuration.eventstore.EventStoreType;
import io.naryo.domain.configuration.eventstore.database.DatabaseEngine;
import io.naryo.infrastructure.configuration.source.env.model.event.store.block.BlockEventStoreConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.event.store.block.EventStoreTargetProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Setter
public final class DatabaseBlockEventStoreConfigurationProperties
        extends BlockEventStoreConfigurationProperties
        implements DatabaseBlockEventStoreConfigurationDescriptor {

    private @NotNull DatabaseEngine engine;

    public DatabaseBlockEventStoreConfigurationProperties(
            Set<EventStoreTargetProperties> targets,
            @Nullable Map<String, Object> additionalProperties,
            @Nullable ConfigurationSchema propertiesSchema,
            @NotNull DatabaseEngine engine) {
        super(EventStoreType.DATABASE, targets, additionalProperties, propertiesSchema);
        this.engine = engine;
    }

    @Override
    public DatabaseEngine getEngine() {
        return engine;
    }
}
