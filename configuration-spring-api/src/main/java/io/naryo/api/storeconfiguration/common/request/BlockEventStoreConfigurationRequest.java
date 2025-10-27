package io.naryo.api.storeconfiguration.common.request;

import java.util.Set;

import io.naryo.application.configuration.source.model.store.event.BlockEventStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.store.event.EventStoreTargetDescriptor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Schema(description = "Block event store configuration request")
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class BlockEventStoreConfigurationRequest
        extends EventStoreConfigurationRequest
        implements BlockEventStoreConfigurationDescriptor {

    @NotNull private Set<EventStoreTargetRequest> targets;

    @Override
    public Set<EventStoreTargetRequest> getTargets() {
        return targets;
    }

    @Override
    public void setTargets(Set<? extends EventStoreTargetDescriptor> targets) {}
}
