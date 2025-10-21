package io.naryo.api.storeconfiguration.common.response;

import io.naryo.domain.configuration.store.active.feature.event.block.EventStoreTarget;
import io.naryo.domain.configuration.store.active.feature.event.block.TargetType;

public record EventStoreTargetResponse(TargetType type, String destination) {

    static EventStoreTargetResponse map(EventStoreTarget eventStoreTarget) {
        return new EventStoreTargetResponse(
                eventStoreTarget.type(), eventStoreTarget.destination().value());
    }
}
