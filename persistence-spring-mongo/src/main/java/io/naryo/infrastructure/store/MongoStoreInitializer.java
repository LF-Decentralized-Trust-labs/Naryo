package io.naryo.infrastructure.store;

import java.util.List;
import java.util.Map;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaType;
import io.naryo.application.configuration.source.model.store.ActiveStoreConfigurationDescriptor;
import io.naryo.application.store.configuration.mapper.ActiveStoreConfigurationAdditionalPropertiesMapperRegistry;
import io.naryo.application.store.configuration.mapper.ActiveStoreConfigurationDescriptorMapper;
import io.naryo.application.store.configuration.mapper.ActiveStoreConfigurationMapperRegistry;
import io.naryo.application.store.configuration.normalization.ActiveStoreConfigurationNormalizerRegistry;
import io.naryo.domain.configuration.store.MongoStoreConfiguration;
import io.naryo.domain.configuration.store.active.BaseActiveStoreNormalizer;
import io.naryo.infrastructure.configuration.beans.env.EnvironmentInitializer;
import org.springframework.stereotype.Component;

import static io.naryo.domain.MongoConstants.MONGO_TYPE;

@Component
public final class MongoStoreInitializer implements EnvironmentInitializer {

    private final ActiveStoreConfigurationNormalizerRegistry normalizerRegistry;
    private final ActiveStoreConfigurationMapperRegistry mapperRegistry;
    private final ActiveStoreConfigurationAdditionalPropertiesMapperRegistry
            additionalPropertiesMapperRegistry;
    private final ConfigurationSchemaRegistry schemaRegistry;

    public MongoStoreInitializer(
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
                MONGO_TYPE,
                ActiveStoreConfigurationDescriptor.class,
                properties ->
                        new MongoStoreConfiguration(
                                properties.getNodeId(),
                                ActiveStoreConfigurationDescriptorMapper.map(properties)));

        additionalPropertiesMapperRegistry.register(
                MONGO_TYPE,
                MongoStoreConfiguration.class,
                mongoActiveStoreConfiguration -> Map.of());

        schemaRegistry.register(
                ConfigurationSchemaType.STORE,
                MONGO_TYPE,
                new ConfigurationSchema(MONGO_TYPE, List.of()));

        var normalizer =
                new BaseActiveStoreNormalizer<MongoStoreConfiguration>() {
                    @Override
                    public MongoStoreConfiguration normalize(MongoStoreConfiguration in) {
                        var normalizedFeatures = normalize(in.getFeatures());
                        return in.toBuilder().features(normalizedFeatures).build();
                    }
                };
        normalizerRegistry.register(
                MONGO_TYPE,
                MongoStoreConfiguration.class,
                configuration -> {
                    if (configuration instanceof MongoStoreConfiguration mongoConf) {
                        return normalizer.normalize(mongoConf);
                    }
                    return configuration;
                });
    }
}
