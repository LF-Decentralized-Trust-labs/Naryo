package io.naryo.application.http.configuration.manager;

import java.util.*;
import java.util.function.BinaryOperator;

import io.naryo.application.configuration.manager.BaseConfigurationManager;
import io.naryo.application.configuration.provider.SourceProvider;
import io.naryo.application.configuration.source.model.http.HttpClientDescriptor;
import io.naryo.application.configuration.source.model.http.factory.HttpClientFactory;
import io.naryo.domain.common.http.HttpClient;

public final class DefaultHttpClientConfigurationManager
        extends BaseConfigurationManager<HttpClient, HttpClientDescriptor>
        implements HttpClientConfigurationManager {

    public HttpClientFactory httpClientFactory;

    public DefaultHttpClientConfigurationManager(
            List<? extends SourceProvider<HttpClientDescriptor>> collectionSourceProviders,
            HttpClientFactory httpClientFactory) {
        super(collectionSourceProviders);
        this.httpClientFactory = httpClientFactory;
    }

    @Override
    protected BinaryOperator<HttpClientDescriptor> mergeFunction() {
        return HttpClientDescriptor::merge;
    }

    @Override
    protected HttpClient map(HttpClientDescriptor source) {
        return httpClientFactory.create(source);
    }
}
