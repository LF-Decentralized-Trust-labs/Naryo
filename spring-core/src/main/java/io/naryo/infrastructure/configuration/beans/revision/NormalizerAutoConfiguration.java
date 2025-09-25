package io.naryo.infrastructure.configuration.beans.revision;

import io.naryo.application.broadcaster.configuration.normalization.BroadcasterConfigurationNormalizerRegistry;
import io.naryo.application.store.configuration.normalization.ActiveStoreConfigurationNormalizerRegistry;
import io.naryo.domain.broadcaster.BroadcasterNormalizer;
import io.naryo.domain.common.http.HttpClientNormalizer;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfigurationNormalizer;
import io.naryo.domain.configuration.store.StoreConfigurationNormalizer;
import io.naryo.domain.configuration.store.active.ActiveStoreConfigurationNormalizer;
import io.naryo.domain.filter.FilterNormalizer;
import io.naryo.domain.node.NodeNormalizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NormalizerAutoConfiguration {

    @Bean
    public NodeNormalizer nodeNormalizer() {
        return new NodeNormalizer();
    }

    @Bean
    public FilterNormalizer filterNormalizer() {
        return new FilterNormalizer();
    }

    @Bean
    public BroadcasterNormalizer broadcasterNormalizer() {
        return new BroadcasterNormalizer();
    }

    @Bean
    public BroadcasterConfigurationNormalizer broadcasterConfigurationNormalizer(
            BroadcasterConfigurationNormalizerRegistry registry) {
        return new BroadcasterConfigurationNormalizer(registry);
    }

    @Bean
    public ActiveStoreConfigurationNormalizer activeStoreConfigurationNormalizer(
            ActiveStoreConfigurationNormalizerRegistry registry) {
        return new ActiveStoreConfigurationNormalizer(registry);
    }

    @Bean
    public StoreConfigurationNormalizer storeConfigurationNormalizer(
            ActiveStoreConfigurationNormalizer activeNormalizer) {
        return new StoreConfigurationNormalizer(activeNormalizer);
    }

    @Bean
    public HttpClientNormalizer httpClientNormalizer() {
        return new HttpClientNormalizer();
    }
}
