package io.naryo.infrastructure.configuration.beans.store.configuration;

import java.util.List;

import io.naryo.application.store.configuration.manager.DefaultStoreConfigurationManager;
import io.naryo.application.store.configuration.manager.StoreConfigurationManager;
import io.naryo.application.store.configuration.mapper.ActiveStoreConfigurationMapperRegistry;
import io.naryo.application.store.configuration.mapper.StoreConfigurationDescriptorMapper;
import io.naryo.application.store.configuration.normalization.ActiveStoreConfigurationNormalizerRegistry;
import io.naryo.application.store.configuration.provider.StoreSourceProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StoreAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(StoreConfigurationDescriptorMapper.class)
    public StoreConfigurationDescriptorMapper storeConfigurationDescriptorToDomainMapper(
            ActiveStoreConfigurationMapperRegistry registry) {
        return new StoreConfigurationDescriptorMapper(registry);
    }

    @Bean
    @ConditionalOnMissingBean(StoreConfigurationManager.class)
    public StoreConfigurationManager storeConfigurationManager(
            List<StoreSourceProvider> providers,
            StoreConfigurationDescriptorMapper storeConfigurationDescriptorMapper) {
        return new DefaultStoreConfigurationManager(
                providers, storeConfigurationDescriptorMapper);
    }

    @Bean
    @ConditionalOnMissingBean(ActiveStoreConfigurationMapperRegistry.class)
    public ActiveStoreConfigurationMapperRegistry storeConfigurationMapperRegistry() {
        return new ActiveStoreConfigurationMapperRegistry();
    }

    @Bean
    @ConditionalOnMissingBean(ActiveStoreConfigurationNormalizerRegistry.class)
    public ActiveStoreConfigurationNormalizerRegistry activeStoreConfigurationNormalizerRegistry() {
        return new ActiveStoreConfigurationNormalizerRegistry();
    }
}
