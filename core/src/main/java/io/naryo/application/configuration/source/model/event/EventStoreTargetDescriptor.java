package io.naryo.application.configuration.source.model.event;

import io.naryo.domain.configuration.eventstore.block.TargetType;

public interface EventStoreTargetDescriptor {

    TargetType type();

    String destination();
}
