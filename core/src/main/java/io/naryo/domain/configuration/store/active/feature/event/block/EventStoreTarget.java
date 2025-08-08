package io.naryo.domain.configuration.store.active.feature.event.block;

import io.naryo.domain.common.Destination;

public record EventStoreTarget(TargetType type, Destination destination) {}
