package io.naryo.infrastructure.store;

import java.util.List;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaType;
import io.naryo.application.configuration.source.mapper.StoreConfigurationDescriptorMapper;
import io.naryo.application.configuration.source.model.store.ActiveStoreConfigurationDescriptor;
import io.naryo.application.store.configuration.mapper.ActiveStoreConfigurationMapperRegistry;
import io.naryo.application.store.configuration.normalization.ActiveStoreConfigurationNormalizerRegistry;
import io.naryo.domain.configuration.store.active.BaseActiveStoreNormalizer;
import io.naryo.domain.configuration.store.active.JpaActiveStoreConfiguration;
import io.naryo.infrastructure.configuration.beans.env.EnvironmentInitializer;
import org.springframework.stereotype.Component;

import static io.naryo.domain.JpaConstants.JPA_TYPE;

@Component
public final class JpaStoreInitializer implements EnvironmentInitializer {

    private final ActiveStoreConfigurationNormalizerRegistry normalizerRegistry;
    private final ActiveStoreConfigurationMapperRegistry mapperRegistry;
    private final ConfigurationSchemaRegistry schemaRegistry;

    public JpaStoreInitializer(
            ActiveStoreConfigurationNormalizerRegistry normalizerRegistry,
            ActiveStoreConfigurationMapperRegistry mapperRegistry,
            ConfigurationSchemaRegistry schemaRegistry) {
        this.normalizerRegistry = normalizerRegistry;
        this.mapperRegistry = mapperRegistry;
        this.schemaRegistry = schemaRegistry;
    }

    @Override
    public void initialize() {
        mapperRegistry.register(
                JPA_TYPE,
                ActiveStoreConfigurationDescriptor.class,
                properties ->
                        new JpaActiveStoreConfiguration(
                                properties.getNodeId(),
                                StoreConfigurationDescriptorMapper.map(properties)));

        schemaRegistry.register(
                ConfigurationSchemaType.STORE,
                JPA_TYPE,
                new ConfigurationSchema(JPA_TYPE, List.of()));

        var normalizer =
                new BaseActiveStoreNormalizer<JpaActiveStoreConfiguration>() {
                    @Override
                    public JpaActiveStoreConfiguration normalize(JpaActiveStoreConfiguration in) {
                        var normalizedFeatures = normalize(in.getFeatures());
                        return in.toBuilder().features(normalizedFeatures).build();
                    }
                };
        normalizerRegistry.register(
                JPA_TYPE,
                JpaActiveStoreConfiguration.class,
                configuration -> {
                    if (configuration instanceof JpaActiveStoreConfiguration jpaConf) {
                        return normalizer.normalize(jpaConf);
                    }
                    return configuration;
                });
    }
}
