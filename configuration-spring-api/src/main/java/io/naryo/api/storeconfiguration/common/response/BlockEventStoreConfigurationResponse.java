package io.naryo.api.storeconfiguration.common.response;

import java.util.Set;
import java.util.stream.Collectors;

import io.naryo.domain.configuration.store.active.feature.event.EventStoreStrategy;
import io.naryo.domain.configuration.store.active.feature.event.block.BlockEventStoreConfiguration;
import lombok.Getter;

@Getter
public class BlockEventStoreConfigurationResponse extends EventStoreConfigurationResponse {

    private final Set<EventStoreTargetResponse> targets;

    public BlockEventStoreConfigurationResponse(
            EventStoreStrategy strategy, Set<EventStoreTargetResponse> targets) {
        super(strategy);
        this.targets = targets;
    }

    static BlockEventStoreConfigurationResponse map(
            BlockEventStoreConfiguration blockEventStoreConfiguration) {
        Set<EventStoreTargetResponse> targets =
                blockEventStoreConfiguration.getTargets().stream()
                        .map(EventStoreTargetResponse::map)
                        .collect(Collectors.toSet());

        return new BlockEventStoreConfigurationResponse(
                blockEventStoreConfiguration.getStrategy(), targets);
    }
}
