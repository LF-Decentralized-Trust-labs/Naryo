package io.naryo.infrastructure.configuration.beans.broadcaster.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.infrastructure.broadcaster.http.producer.HttpBroadcasterProducer;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpBroadcasterAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(HttpBroadcasterProducer.class)
    public HttpBroadcasterProducer httpBroadcasterProducer(
            OkHttpClient httpClient, ObjectMapper objectMapper) {
        return new HttpBroadcasterProducer(httpClient, objectMapper);
    }
}
