package io.naryo.infrastructure.configuration.beans.store.http;

import java.util.List;
import java.util.Map;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.FieldDefinition;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaType;
import io.naryo.application.configuration.source.model.store.ActiveStoreConfigurationDescriptor;
import io.naryo.application.store.configuration.mapper.ActiveStoreConfigurationAdditionalPropertiesMapperRegistry;
import io.naryo.application.store.configuration.mapper.ActiveStoreConfigurationDescriptorMapper;
import io.naryo.application.store.configuration.mapper.ActiveStoreConfigurationMapperRegistry;
import io.naryo.application.store.configuration.normalization.ActiveStoreConfigurationNormalizerRegistry;
import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;
import io.naryo.domain.common.connection.endpoint.ConnectionEndpointNormalizer;
import io.naryo.domain.configuration.store.active.BaseActiveStoreNormalizer;
import io.naryo.domain.configuration.store.active.http.HttpStoreConfiguration;
import io.naryo.infrastructure.configuration.beans.env.EnvironmentInitializer;
import org.springframework.stereotype.Component;

import static io.naryo.domain.HttpConstants.HTTP_TYPE;

@Component
public final class HttpStoreInitializer implements EnvironmentInitializer {

    private static final String FIELD_ENDPOINT = "endpoint";

    private final ActiveStoreConfigurationNormalizerRegistry normalizerRegistry;
    private final ActiveStoreConfigurationMapperRegistry mapperRegistry;
    private final ActiveStoreConfigurationAdditionalPropertiesMapperRegistry
            additionalPropertiesMapperRegistry;
    private final ConfigurationSchemaRegistry schemaRegistry;

    public HttpStoreInitializer(
            ActiveStoreConfigurationNormalizerRegistry normalizerRegistry,
            ActiveStoreConfigurationMapperRegistry mapperRegistry,
            ActiveStoreConfigurationAdditionalPropertiesMapperRegistry
                    additionalPropertiesMapperRegistry,
            ConfigurationSchemaRegistry schemaRegistry) {
        this.normalizerRegistry = normalizerRegistry;
        this.mapperRegistry = mapperRegistry;
        this.additionalPropertiesMapperRegistry = additionalPropertiesMapperRegistry;
        this.schemaRegistry = schemaRegistry;
    }

    @Override
    public void initialize() {
        mapperRegistry.register(
                HTTP_TYPE,
                ActiveStoreConfigurationDescriptor.class,
                properties ->
                        new HttpStoreConfiguration(
                                properties.getNodeId(),
                                ActiveStoreConfigurationDescriptorMapper.map(properties),
                                new ConnectionEndpoint(
                                        !properties.getAdditionalProperties().isEmpty()
                                                ? ((HttpStoreEndpoint)
                                                                properties
                                                                        .getAdditionalProperties()
                                                                        .get(FIELD_ENDPOINT))
                                                        .url()
                                                : null)));

        additionalPropertiesMapperRegistry.register(
                HTTP_TYPE,
                HttpStoreConfiguration.class,
                httpStoreConfiguration ->
                        Map.of(
                                FIELD_ENDPOINT,
                                new HttpStoreEndpoint(
                                        httpStoreConfiguration.getEndpoint().getUrl())));

        schemaRegistry.register(
                ConfigurationSchemaType.STORE,
                HTTP_TYPE,
                new ConfigurationSchema(
                        HTTP_TYPE,
                        List.of(
                                new FieldDefinition(
                                        FIELD_ENDPOINT, HttpStoreEndpoint.class, true, null))));

        var normalizer =
                new BaseActiveStoreNormalizer<HttpStoreConfiguration>() {
                    @Override
                    public HttpStoreConfiguration normalize(HttpStoreConfiguration in) {
                        var normalizedFeatures = normalize(in.getFeatures());
                        var endpoint =
                                ConnectionEndpointNormalizer.INSTANCE.normalize(in.getEndpoint());
                        return in.toBuilder()
                                .features(normalizedFeatures)
                                .endpoint(endpoint)
                                .build();
                    }
                };
        normalizerRegistry.register(
                HTTP_TYPE,
                HttpStoreConfiguration.class,
                configuration -> {
                    if (configuration instanceof HttpStoreConfiguration httpConf) {
                        return normalizer.normalize(httpConf);
                    }
                    return configuration;
                });
    }

    public record HttpStoreEndpoint(String url) {}
}
