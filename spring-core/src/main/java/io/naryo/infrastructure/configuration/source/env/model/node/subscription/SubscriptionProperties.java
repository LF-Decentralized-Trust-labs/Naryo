package io.naryo.infrastructure.configuration.source.env.model.node.subscription;

import io.naryo.domain.node.subscription.SubscriptionStrategy;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public abstract class SubscriptionProperties {

    private final @Getter @NotNull SubscriptionStrategy strategy;

    public SubscriptionProperties(SubscriptionStrategy strategy) {
        this.strategy = strategy;
    }
}
