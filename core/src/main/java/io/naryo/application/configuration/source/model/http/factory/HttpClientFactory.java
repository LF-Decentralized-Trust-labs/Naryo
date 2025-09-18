package io.naryo.application.configuration.source.model.http.factory;

import io.naryo.application.configuration.source.model.http.HttpClientDescriptor;
import io.naryo.domain.common.http.HttpClient;

public interface HttpClientFactory {
    HttpClient create(HttpClientDescriptor descriptor);
}
