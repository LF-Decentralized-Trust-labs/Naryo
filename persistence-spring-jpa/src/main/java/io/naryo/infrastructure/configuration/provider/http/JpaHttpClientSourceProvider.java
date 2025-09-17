package io.naryo.infrastructure.configuration.provider.http;

import io.naryo.application.configuration.source.model.http.HttpClientDescriptor;
import io.naryo.application.http.configuration.provider.HttpClientSourceProvider;
import io.naryo.infrastructure.configuration.persistence.repository.http.HttpClientEntityRepository;
import org.springframework.stereotype.Component;

@Component
public class JpaHttpClientSourceProvider implements HttpClientSourceProvider {

    private final HttpClientEntityRepository repository;

    public JpaHttpClientSourceProvider(HttpClientEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public HttpClientDescriptor load() {
        return repository.findAll().stream().findFirst().orElse(null);
    }

    @Override
    public int priority() {
        return 0;
    }
}
