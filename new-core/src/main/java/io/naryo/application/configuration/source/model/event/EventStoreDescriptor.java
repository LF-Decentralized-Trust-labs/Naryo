package io.naryo.application.configuration.source.model.event;

import java.util.UUID;

import io.naryo.application.configuration.source.model.MergeableDescriptor;
import io.naryo.domain.configuration.eventstore.EventStoreType;

public interface EventStoreDescriptor extends MergeableDescriptor<EventStoreDescriptor> {

    UUID getId();

    EventStoreType getType();
}
