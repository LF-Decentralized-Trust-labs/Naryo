package io.librevents.infrastructure.configuration.http;

import java.util.concurrent.TimeUnit;

import io.librevents.infrastructure.configuration.MainProperties;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnMissingBean(OkHttpClient.class)
public class HttpClientAutoConfiguration {

    @Bean
    public OkHttpClient httpClient(MainProperties properties) {
        HttpClientProperties config = properties.httpClient();

        return new OkHttpClient.Builder()
                .connectionPool(
                        new ConnectionPool(
                                config.maxIdleConnections(),
                                config.keepAliveDuration().getSeconds(),
                                TimeUnit.SECONDS))
                .connectTimeout(config.connectTimeout())
                .readTimeout(config.readTimeout())
                .writeTimeout(config.writeTimeout())
                .callTimeout(config.callTimeout())
                .pingInterval(config.pingInterval())
                .retryOnConnectionFailure(config.isRetryOnConnectionFailure())
                .build();
    }
}
