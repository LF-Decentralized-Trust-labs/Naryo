package io.naryo.infrastructure.configuration.beans.broadcaster.rabbitmq;

import java.util.List;
import java.util.Map;

import io.naryo.application.broadcaster.configuration.mapper.BroadcasterConfigurationMapperRegistry;
import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.FieldDefinition;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaType;
import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterConfigurationDescriptor;
import io.naryo.domain.configuration.broadcaster.BroadcasterCache;
import io.naryo.domain.configuration.broadcaster.rabbitmq.Exchange;
import io.naryo.domain.configuration.broadcaster.rabbitmq.RabbitMqBroadcasterConfiguration;
import io.naryo.domain.configuration.broadcaster.rabbitmq.RoutingKey;
import io.naryo.infrastructure.configuration.beans.env.EnvironmentInitializer;
import org.springframework.stereotype.Component;

import static io.naryo.application.common.util.OptionalUtil.valueOrNull;
import static io.naryo.domain.RabbitMqConstants.RABBITMQ_TYPE;

@Component
public final class RabbitMqBroadcasterInitializer implements EnvironmentInitializer {

    private final BroadcasterConfigurationMapperRegistry mapperRegistry;
    private final ConfigurationSchemaRegistry schemaRegistry;

    public RabbitMqBroadcasterInitializer(
            BroadcasterConfigurationMapperRegistry mapperRegistry,
            ConfigurationSchemaRegistry schemaRegistry) {
        this.mapperRegistry = mapperRegistry;
        this.schemaRegistry = schemaRegistry;
    }

    @Override
    public void initialize() {
        mapperRegistry.register(
                RABBITMQ_TYPE,
                BroadcasterConfigurationDescriptor.class,
                properties -> {
                    Map<String, Object> props = properties.getAdditionalProperties();
                    Object raw = props.isEmpty() ? null : props.get("destination");
                    RabbitMqDestination destination = (RabbitMqDestination) raw;

                    return new RabbitMqBroadcasterConfiguration(
                            properties.getId(),
                            new BroadcasterCache(
                                    valueOrNull(properties.getCache()).getExpirationTime()),
                            new Exchange(destination == null ? null : destination.exchange()),
                            new RoutingKey(destination == null ? null : destination.routingKey()));
                });

        schemaRegistry.register(
                ConfigurationSchemaType.BROADCASTER,
                RABBITMQ_TYPE,
                new ConfigurationSchema(
                        RABBITMQ_TYPE,
                        List.of(
                                new FieldDefinition(
                                        "destination", RabbitMqDestination.class, true, null))));
    }

    public record RabbitMqDestination(String exchange, String routingKey) {}
}
