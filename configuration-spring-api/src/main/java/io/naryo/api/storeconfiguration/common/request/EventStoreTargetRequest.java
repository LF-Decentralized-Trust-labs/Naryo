package io.naryo.api.storeconfiguration.common.request;

import io.naryo.application.configuration.source.model.store.event.EventStoreTargetDescriptor;
import io.naryo.domain.configuration.store.active.feature.event.block.TargetType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@EqualsAndHashCode
public class EventStoreTargetRequest implements EventStoreTargetDescriptor {

    @NotNull private TargetType type;

    @NotNull private String destination;

    @Override
    public TargetType type() {
        return type;
    }

    @Override
    public String destination() {
        return destination;
    }
}
