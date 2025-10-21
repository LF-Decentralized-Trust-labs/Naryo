package io.naryo.api.storeconfiguration.common.response;

import io.naryo.domain.configuration.store.active.feature.filter.FilterStoreConfiguration;
import lombok.Getter;

@Getter
public class FilterStoreConfigurationResponse extends StoreFeatureConfigurationResponse {

    private final String destination;

    public FilterStoreConfigurationResponse(String destination) {
        this.destination = destination;
    }

    static FilterStoreConfigurationResponse map(FilterStoreConfiguration filterStoreConfiguration) {
        return new FilterStoreConfigurationResponse(
                filterStoreConfiguration.getDestination().value());
    }
}
