package io.naryo.api.storeconfiguration.getAll.model;

import java.util.Set;
import java.util.stream.Collectors;

import io.naryo.domain.configuration.store.active.feature.event.EventStoreStrategy;
import io.naryo.domain.configuration.store.active.feature.event.block.BlockEventStoreConfiguration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Block event store configuration")
@Getter
public class BlockEventStoreConfigurationResponse extends EventStoreConfigurationResponse {

    private final Set<EventStoreTargetResponse> targets;

    public BlockEventStoreConfigurationResponse(
            EventStoreStrategy strategy, Set<EventStoreTargetResponse> targets) {
        super(strategy);
        this.targets = targets;
    }

    static BlockEventStoreConfigurationResponse fromDomain(
            BlockEventStoreConfiguration blockEventStoreConfiguration) {
        Set<EventStoreTargetResponse> targets =
                blockEventStoreConfiguration.getTargets().stream()
                        .map(EventStoreTargetResponse::fromDomain)
                        .collect(Collectors.toSet());

        return new BlockEventStoreConfigurationResponse(
                blockEventStoreConfiguration.getStrategy(), targets);
    }
}
