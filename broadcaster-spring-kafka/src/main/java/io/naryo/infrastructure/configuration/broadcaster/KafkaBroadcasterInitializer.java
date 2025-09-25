package io.naryo.infrastructure.configuration.broadcaster;

import java.util.List;

import io.naryo.application.broadcaster.configuration.mapper.BroadcasterConfigurationMapperRegistry;
import io.naryo.application.broadcaster.configuration.normalization.BroadcasterConfigurationNormalizerRegistry;
import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaType;
import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterConfigurationDescriptor;
import io.naryo.domain.configuration.broadcaster.*;
import io.naryo.infrastructure.configuration.beans.env.EnvironmentInitializer;
import org.springframework.stereotype.Component;

import static io.naryo.application.common.util.OptionalUtil.valueOrNull;
import static io.naryo.domain.KafkaConstants.KAFKA_TYPE;

@Component
public final class KafkaBroadcasterInitializer implements EnvironmentInitializer {

    private final BroadcasterConfigurationNormalizerRegistry normalizerRegistry;
    private final BroadcasterConfigurationMapperRegistry mapperRegistry;
    private final ConfigurationSchemaRegistry schemaRegistry;

    public KafkaBroadcasterInitializer(
            BroadcasterConfigurationNormalizerRegistry normalizerRegistry,
            BroadcasterConfigurationMapperRegistry mapperRegistry,
            ConfigurationSchemaRegistry schemaRegistry) {
        this.normalizerRegistry = normalizerRegistry;
        this.mapperRegistry = mapperRegistry;
        this.schemaRegistry = schemaRegistry;
    }

    @Override
    public void initialize() {
        mapperRegistry.register(
                KAFKA_TYPE,
                BroadcasterConfigurationDescriptor.class,
                properties ->
                        new KafkaBroadcasterConfiguration(
                                properties.getId(),
                                new BroadcasterCache(
                                        valueOrNull(properties.getCache()).getExpirationTime())));

        schemaRegistry.register(
                ConfigurationSchemaType.BROADCASTER,
                KAFKA_TYPE,
                new ConfigurationSchema(KAFKA_TYPE, List.of()));

        normalizerRegistry.register(KAFKA_TYPE, KafkaBroadcasterConfiguration.class, in -> in);
    }
}
