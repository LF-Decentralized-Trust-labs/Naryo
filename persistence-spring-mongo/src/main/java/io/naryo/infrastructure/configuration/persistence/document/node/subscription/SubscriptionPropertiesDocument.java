package io.naryo.infrastructure.configuration.persistence.document.node.subscription;

import io.naryo.domain.node.subscription.SubscriptionStrategy;
import lombok.Getter;

@Getter
public abstract class SubscriptionPropertiesDocument {
    private SubscriptionStrategy strategy;
}
