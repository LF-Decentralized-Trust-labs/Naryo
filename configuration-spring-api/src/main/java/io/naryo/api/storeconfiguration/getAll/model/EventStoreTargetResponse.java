package io.naryo.api.storeconfiguration.getAll.model;

import io.naryo.domain.configuration.store.active.feature.event.block.EventStoreTarget;
import io.naryo.domain.configuration.store.active.feature.event.block.TargetType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Event store target")
public record EventStoreTargetResponse(TargetType type, String destination) {

    static EventStoreTargetResponse fromDomain(EventStoreTarget eventStoreTarget) {
        return new EventStoreTargetResponse(
                eventStoreTarget.type(), eventStoreTarget.destination().value());
    }
}
