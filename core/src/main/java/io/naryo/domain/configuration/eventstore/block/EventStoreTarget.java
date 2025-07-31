package io.naryo.domain.configuration.eventstore.block;

import io.naryo.domain.broadcaster.Destination;

public record EventStoreTarget(TargetType type, Destination destination) {}
