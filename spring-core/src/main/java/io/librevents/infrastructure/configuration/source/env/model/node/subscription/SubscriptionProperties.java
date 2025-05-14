package io.librevents.infrastructure.configuration.source.env.model.node.subscription;

import io.librevents.domain.node.subscription.SubscriptionStrategy;

public record SubscriptionProperties(
        SubscriptionStrategy strategy, SubscriptionConfigurationProperties configuration) {}
