package io.librevents.infrastructure.configuration.beans.broadcaster.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.librevents.application.broadcaster.configuration.mapper.BroadcasterConfigurationMapperRegistry;
import io.librevents.domain.common.connection.endpoint.ConnectionEndpoint;
import io.librevents.domain.configuration.broadcaster.BroadcasterCache;
import io.librevents.infrastructure.broadcaster.http.configuration.HttpBroadcasterConfiguration;
import io.librevents.infrastructure.broadcaster.http.producer.HttpBroadcasterProducer;
import io.librevents.infrastructure.configuration.source.env.model.broadcaster.configuration.BroadcasterConfigurationEntryProperties;
import io.librevents.infrastructure.configuration.source.env.model.broadcaster.configuration.http.HttpBroadcasterConfigurationAdditionalProperties;
import io.librevents.infrastructure.configuration.source.env.registry.broadcaster.BroadcasterConfigurationPropertiesRegistry;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpBroadcasterAutoConfiguration {

    public HttpBroadcasterAutoConfiguration(
            BroadcasterConfigurationPropertiesRegistry registry,
            BroadcasterConfigurationMapperRegistry mapperRegistry) {
        registry.register("http", HttpBroadcasterConfigurationAdditionalProperties.class);
        mapperRegistry.register(
                "http",
                BroadcasterConfigurationEntryProperties.class,
                properties ->
                        new HttpBroadcasterConfiguration(
                                properties.id(),
                                new BroadcasterCache(properties.cache().expirationTime()),
                                new ConnectionEndpoint(
                                        ((HttpBroadcasterConfigurationAdditionalProperties)
                                                        properties.configuration())
                                                .endpoint()
                                                .url())));
    }

    @Bean
    @ConditionalOnMissingBean(HttpBroadcasterProducer.class)
    public HttpBroadcasterProducer httpBroadcasterProducer(
            OkHttpClient httpClient, ObjectMapper objectMapper) {
        return new HttpBroadcasterProducer(httpClient, objectMapper);
    }
}
