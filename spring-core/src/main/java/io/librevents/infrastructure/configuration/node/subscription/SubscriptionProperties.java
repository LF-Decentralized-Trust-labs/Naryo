package io.librevents.infrastructure.configuration.node.subscription;

import io.librevents.domain.node.subscription.SubscriptionStrategy;

public record SubscriptionProperties(
        SubscriptionStrategy strategy, SubscriptionConfigurationProperties configuration) {}
