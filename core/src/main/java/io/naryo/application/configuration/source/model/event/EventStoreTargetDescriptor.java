package io.naryo.application.configuration.source.model.event;

import io.naryo.domain.configuration.store.active.feature.event.block.TargetType;

public interface EventStoreTargetDescriptor {

    TargetType type();

    String destination();
}
