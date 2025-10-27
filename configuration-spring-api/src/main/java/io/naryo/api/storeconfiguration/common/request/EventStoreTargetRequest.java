package io.naryo.api.storeconfiguration.common.request;

import io.naryo.application.configuration.source.model.store.event.EventStoreTargetDescriptor;
import io.naryo.domain.configuration.store.active.feature.event.block.TargetType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@Schema(description = "Event store target request")
@AllArgsConstructor
@EqualsAndHashCode
public class EventStoreTargetRequest implements EventStoreTargetDescriptor {

    @NotNull private TargetType type;

    @NotNull private String destination;

    @Override
    public TargetType getType() {
        return type;
    }

    @Override
    public String getDestination() {
        return destination;
    }
}
