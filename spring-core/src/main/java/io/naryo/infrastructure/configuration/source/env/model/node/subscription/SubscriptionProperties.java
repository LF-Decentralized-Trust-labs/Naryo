package io.naryo.infrastructure.configuration.source.env.model.node.subscription;

import io.naryo.domain.node.subscription.SubscriptionStrategy;
import io.naryo.infrastructure.configuration.source.env.model.node.subscription.block.BlockSubscriptionConfigurationProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record SubscriptionProperties(
        @NotNull SubscriptionStrategy strategy,
        @Valid @NotNull SubscriptionConfigurationProperties configuration) {

    public SubscriptionProperties() {
        this(SubscriptionStrategy.BLOCK_BASED, new BlockSubscriptionConfigurationProperties());
    }
}
