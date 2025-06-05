package io.librevents.infrastructure.configuration.source.env.model.node.subscription.block.method;

import io.librevents.domain.node.subscription.block.method.BlockSubscriptionMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record BlockSubscriptionMethodProperties(
        @NotNull BlockSubscriptionMethod type,
        @Valid @NotNull BlockSubscriptionMethodConfigurationProperties configuration) {

    private static final BlockSubscriptionMethod DEFAULT_METHOD = BlockSubscriptionMethod.POLL;

    public BlockSubscriptionMethodProperties() {
        this(DEFAULT_METHOD, new PollBlockSubscriptionMethodConfigurationProperties());
    }
}
