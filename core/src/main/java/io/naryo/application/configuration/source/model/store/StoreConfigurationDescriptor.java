package io.naryo.application.configuration.source.model.store;

import java.util.UUID;

import io.naryo.application.configuration.source.model.MergeableDescriptor;
import io.naryo.domain.configuration.store.StoreState;

public interface StoreConfigurationDescriptor
        extends MergeableDescriptor<StoreConfigurationDescriptor> {

    UUID getNodeId();

    StoreState getState();

    @Override
    default StoreConfigurationDescriptor merge(StoreConfigurationDescriptor other) {
        if (other == null) {
            return this;
        }

        if (!this.getState().equals(other.getState())) {
            return other;
        }

        return this;
    }
}
