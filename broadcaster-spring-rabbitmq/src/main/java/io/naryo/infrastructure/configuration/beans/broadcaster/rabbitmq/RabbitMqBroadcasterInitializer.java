package io.naryo.infrastructure.configuration.beans.broadcaster.rabbitmq;

import java.util.List;
import java.util.Map;

import io.naryo.application.broadcaster.configuration.mapper.BroadcasterConfigurationMapperRegistry;
import io.naryo.application.broadcaster.configuration.normalization.BroadcasterConfigurationNormalizerRegistry;
import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.FieldDefinition;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaType;
import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterConfigurationDescriptor;
import io.naryo.domain.common.normalization.NormalizationUtil;
import io.naryo.domain.configuration.broadcaster.BroadcasterCache;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.configuration.broadcaster.rabbitmq.Exchange;
import io.naryo.domain.configuration.broadcaster.rabbitmq.RabbitMqBroadcasterConfiguration;
import io.naryo.domain.normalization.Normalizer;
import io.naryo.infrastructure.configuration.beans.env.EnvironmentInitializer;
import org.springframework.stereotype.Component;

import static io.naryo.application.common.util.OptionalUtil.valueOrNull;
import static io.naryo.domain.RabbitMqConstants.RABBITMQ_TYPE;

@Component
public final class RabbitMqBroadcasterInitializer implements EnvironmentInitializer {

    private final BroadcasterConfigurationNormalizerRegistry normalizerRegistry;
    private final BroadcasterConfigurationMapperRegistry mapperRegistry;
    private final ConfigurationSchemaRegistry schemaRegistry;

    public RabbitMqBroadcasterInitializer(
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
                RABBITMQ_TYPE,
                BroadcasterConfigurationDescriptor.class,
                properties -> {
                    Map<String, Object> props = properties.getAdditionalProperties();
                    Object raw = props.isEmpty() ? null : props.get("exchange");
                    Exchange exchange = (Exchange) raw;

                    return new RabbitMqBroadcasterConfiguration(
                            properties.getId(),
                            new BroadcasterCache(
                                    valueOrNull(properties.getCache()).getExpirationTime()),
                            exchange);
                });

        schemaRegistry.register(
                ConfigurationSchemaType.BROADCASTER,
                RABBITMQ_TYPE,
                new ConfigurationSchema(
                        RABBITMQ_TYPE,
                        List.of(new FieldDefinition("exchange", Exchange.class, true, null))));

        var normalizer =
                (Normalizer<BroadcasterConfiguration>)
                        in -> {
                            if (in instanceof RabbitMqBroadcasterConfiguration typed) {
                                return typed.toBuilder()
                                        .exchange(
                                                new Exchange(
                                                        NormalizationUtil.normalize(
                                                                typed.getExchange().value())))
                                        .build();
                            }
                            return in;
                        };
        normalizerRegistry.register(
                RABBITMQ_TYPE, RabbitMqBroadcasterConfiguration.class, normalizer);
    }
}
