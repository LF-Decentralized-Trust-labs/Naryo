package io.naryo.api.storeconfiguration.common.request;

import io.naryo.api.RequestBuilder;
import org.instancio.Instancio;

public class FilterStoreFeatureConfigurationRequestBuilder
        implements RequestBuilder<
                FilterStoreFeatureConfigurationRequestBuilder,
        FilterStoreConfigurationRequest> {

    private String destination;

    @Override
    public FilterStoreFeatureConfigurationRequestBuilder self() {
        return null;
    }

    @Override
    public FilterStoreConfigurationRequest build() {
        return new FilterStoreConfigurationRequest(getDestination());
    }

    public FilterStoreFeatureConfigurationRequestBuilder withDestination(String destination) {
        this.destination = destination;
        return self();
    }

    public String getDestination() {
        return this.destination == null ? Instancio.create(String.class) : this.destination;
    }
}
