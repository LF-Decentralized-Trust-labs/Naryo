package io.naryo.infrastructure.configuration.persistence.document.node.subscription;

import io.naryo.application.configuration.source.model.node.subscription.SubscriptionDescriptor;
import io.naryo.domain.node.subscription.SubscriptionStrategy;
import lombok.Getter;

@Getter
public abstract class SubscriptionPropertiesDocument implements SubscriptionDescriptor {
    private SubscriptionStrategy strategy;
}
