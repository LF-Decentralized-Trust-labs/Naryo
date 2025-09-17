package io.naryo.infrastructure.configuration.beans.http;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.naryo.application.configuration.source.model.http.factory.DefaultHttpClientFactory;
import io.naryo.application.configuration.source.model.http.factory.HttpClientFactory;
import io.naryo.application.http.configuration.manager.DefaultHttpClientConfigurationManager;
import io.naryo.application.http.configuration.manager.HttpClientConfigurationManager;
import io.naryo.application.http.configuration.provider.HttpClientSourceProvider;
import io.naryo.domain.common.http.HttpClient;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnMissingBean(OkHttpClient.class)
public class HttpClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(HttpClientFactory.class)
    public HttpClientFactory httpClientFactory() {
        return new DefaultHttpClientFactory();
    }

    @Bean
    @ConditionalOnMissingBean(HttpClientConfigurationManager.class)
    public HttpClientConfigurationManager httpClientConfigurationManager(
            List<HttpClientSourceProvider> providers, HttpClientFactory httpClientFactory) {
        return new DefaultHttpClientConfigurationManager(providers, httpClientFactory);
    }

    @Bean
    @ConditionalOnMissingBean(OkHttpClient.class)
    public OkHttpClient httpClient(HttpClientConfigurationManager httpClientConfigurationManager) {
        HttpClient config = httpClientConfigurationManager.load();
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
                .retryOnConnectionFailure(config.retryOnConnectionFailure())
                .build();
    }
}
