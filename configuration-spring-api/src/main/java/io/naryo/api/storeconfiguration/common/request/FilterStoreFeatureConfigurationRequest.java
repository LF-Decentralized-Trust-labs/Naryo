package io.naryo.api.storeconfiguration.common.request;

import java.util.Optional;

import io.naryo.application.configuration.source.model.store.filter.FilterStoreConfigurationDescriptor;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class FilterStoreFeatureConfigurationRequest extends StoreFeatureConfigurationRequest
        implements FilterStoreConfigurationDescriptor {

    @NotNull private String destination;

    @Override
    public Optional<String> getDestination() {
        return Optional.of(destination);
    }

    @Override
    public void setDestination(String destination) {}
}
