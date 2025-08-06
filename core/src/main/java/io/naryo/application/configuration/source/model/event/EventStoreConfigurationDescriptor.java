package io.naryo.application.configuration.source.model.event;

import java.util.UUID;

import io.naryo.application.configuration.source.model.MergeableDescriptor;
import io.naryo.domain.configuration.store.StoreState;

public interface EventStoreConfigurationDescriptor
        extends MergeableDescriptor<EventStoreConfigurationDescriptor> {

    UUID getNodeId();

    StoreState getState();

    @Override
    default EventStoreConfigurationDescriptor merge(EventStoreConfigurationDescriptor other) {
        if (other == null) {
            return this;
        }

        if (!this.getState().equals(other.getState())) {
            return other;
        }

        return this;
    }
}
