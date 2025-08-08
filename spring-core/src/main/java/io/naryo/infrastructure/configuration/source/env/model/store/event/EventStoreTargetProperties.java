package io.naryo.infrastructure.configuration.source.env.model.store.event;

import io.naryo.application.configuration.source.model.store.event.EventStoreTargetDescriptor;
import io.naryo.domain.configuration.store.active.feature.event.block.TargetType;
import lombok.Getter;

public record EventStoreTargetProperties(@Getter TargetType type, @Getter String destination)
        implements EventStoreTargetDescriptor {}
