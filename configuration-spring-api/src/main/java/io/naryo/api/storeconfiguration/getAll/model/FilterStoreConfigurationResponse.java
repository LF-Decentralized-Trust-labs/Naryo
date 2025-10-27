package io.naryo.api.storeconfiguration.getAll.model;

import io.naryo.domain.configuration.store.active.feature.filter.FilterStoreConfiguration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Filter store feature configuration")
@Getter
public class FilterStoreConfigurationResponse extends StoreFeatureConfigurationResponse {

    private final String destination;

    public FilterStoreConfigurationResponse(String destination) {
        this.destination = destination;
    }

    static FilterStoreConfigurationResponse fromDomain(FilterStoreConfiguration filterStoreConfiguration) {
        return new FilterStoreConfigurationResponse(
                filterStoreConfiguration.getDestination().value());
    }
}
