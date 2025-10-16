package io.naryo.api.storeconfiguration.fixtures;

import io.naryo.api.storeconfiguration.common.request.FilterStoreFeatureConfigurationRequest;
import io.naryo.domain.DomainBuilder;
import org.instancio.Instancio;

public class FilterStoreFeatureConfigurationRequestBuilder
        implements DomainBuilder<
                FilterStoreFeatureConfigurationRequestBuilder,
                FilterStoreFeatureConfigurationRequest> {

    private String destination;

    @Override
    public FilterStoreFeatureConfigurationRequestBuilder self() {
        return null;
    }

    @Override
    public FilterStoreFeatureConfigurationRequest build() {
        return new FilterStoreFeatureConfigurationRequest(getDestination());
    }

    public FilterStoreFeatureConfigurationRequestBuilder withDestination(String destination) {
        this.destination = destination;
        return self();
    }

    public String getDestination() {
        return this.destination == null ? Instancio.create(String.class) : this.destination;
    }
}
