package io.librevents.infrastructure.configuration.broadcaster.configuration.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.librevents.infrastructure.broadcaster.http.configuration.HttpBroadcasterConfigurationProvider;
import io.librevents.infrastructure.broadcaster.http.producer.HttpBroadcasterProducer;
import io.librevents.infrastructure.configuration.broadcaster.BroadcasterTypeRegistry;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpBroadcasterAutoConfiguration {

    public HttpBroadcasterAutoConfiguration(BroadcasterTypeRegistry registry) {
        registry.register("http", HttpBroadcasterConfigurationAdditionalProperties.class);
    }

    @Bean
    @ConditionalOnMissingBean(HttpBroadcasterProducer.class)
    public HttpBroadcasterProducer httpBroadcasterProducer(
            OkHttpClient httpClient, ObjectMapper objectMapper) {
        return new HttpBroadcasterProducer(httpClient, objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean(HttpBroadcasterConfigurationProvider.class)
    public HttpBroadcasterConfigurationProvider httpBroadcasterConfigurationProvider() {
        return new HttpBroadcasterConfigurationProvider();
    }
}
