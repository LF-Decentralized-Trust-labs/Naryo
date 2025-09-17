package io.naryo.infrastructure.configuration.provider.http;

import io.naryo.application.configuration.source.model.http.HttpClientDescriptor;
import io.naryo.application.http.configuration.provider.HttpClientSourceProvider;
import io.naryo.infrastructure.configuration.source.env.model.EnvironmentProperties;
import org.springframework.stereotype.Component;

@Component
public class EnvHttpClientSourceProvider implements HttpClientSourceProvider {

    private final EnvironmentProperties properties;

    public EnvHttpClientSourceProvider(EnvironmentProperties properties) {
        this.properties = properties;
    }

    @Override
    public HttpClientDescriptor load() {
        return properties.httpClient();
    }

    @Override
    public int priority() {
        return 1;
    }
}
