package io.naryo.api.storeconfiguration.common.response;

import io.naryo.api.RequestBuilder;
import org.instancio.Instancio;

public class FilterStoreConfigurationResponseBuilder
        implements RequestBuilder<
                FilterStoreConfigurationResponseBuilder, FilterStoreConfigurationResponse> {

    private String destination;

    @Override
    public FilterStoreConfigurationResponseBuilder self() {
        return null;
    }

    @Override
    public FilterStoreConfigurationResponse build() {
        return new FilterStoreConfigurationResponse(getDestination());
    }

    public FilterStoreConfigurationResponseBuilder withDestination(String destination) {
        this.destination = destination;
        return self();
    }

    public String getDestination() {
        return this.destination == null ? Instancio.create(String.class) : this.destination;
    }
}
