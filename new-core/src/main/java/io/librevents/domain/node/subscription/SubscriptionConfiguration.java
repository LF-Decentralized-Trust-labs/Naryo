package io.librevents.domain.node.subscription;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public abstract class SubscriptionConfiguration {
    protected final SubscriptionStrategy strategy;

    protected SubscriptionConfiguration(SubscriptionStrategy strategy) {
        this.strategy = strategy;
    }
}
