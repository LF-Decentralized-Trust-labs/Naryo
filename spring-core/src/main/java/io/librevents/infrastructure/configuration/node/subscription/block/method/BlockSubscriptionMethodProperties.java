package io.librevents.infrastructure.configuration.node.subscription.block.method;

import io.librevents.domain.node.subscription.block.method.BlockSubscriptionMethod;

public record BlockSubscriptionMethodProperties(
        BlockSubscriptionMethod type,
        BlockSubscriptionMethodConfigurationProperties configuration) {}
