package io.naryo.infrastructure.configuration.persistence.document.node.subscription;

import io.naryo.application.configuration.source.model.node.subscription.SubscriptionDescriptor;
import io.naryo.domain.node.subscription.SubscriptionStrategy;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public abstract class SubscriptionPropertiesDocument implements SubscriptionDescriptor {
    private @Setter @Getter @NotNull SubscriptionStrategy strategy;

    public SubscriptionPropertiesDocument(SubscriptionStrategy strategy) {
        this.strategy = strategy;
    }
}
