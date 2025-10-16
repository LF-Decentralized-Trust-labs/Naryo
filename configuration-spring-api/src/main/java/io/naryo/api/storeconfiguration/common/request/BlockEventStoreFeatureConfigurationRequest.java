package io.naryo.api.storeconfiguration.common.request;

import java.util.Set;

import io.naryo.application.configuration.source.model.store.event.BlockEventStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.store.event.EventStoreTargetDescriptor;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class BlockEventStoreFeatureConfigurationRequest
        extends EventStoreFeatureConfigurationRequest
        implements BlockEventStoreConfigurationDescriptor {

    @NotNull private Set<EventStoreTargetRequest> targets;

    @Override
    public Set<? extends EventStoreTargetDescriptor> getTargets() {
        return targets;
    }

    @Override
    public void setTargets(Set<? extends EventStoreTargetDescriptor> targets) {}
}
