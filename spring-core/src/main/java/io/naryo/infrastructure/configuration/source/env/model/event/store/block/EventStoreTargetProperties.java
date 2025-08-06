package io.naryo.infrastructure.configuration.source.env.model.event.store.block;

import io.naryo.application.configuration.source.model.event.EventStoreTargetDescriptor;
import io.naryo.domain.configuration.store.active.feature.event.block.TargetType;
import lombok.Getter;

public record EventStoreTargetProperties(@Getter TargetType type, @Getter String destination)
        implements EventStoreTargetDescriptor {}
