package io.librevents.infrastructure.configuration.source.env.model.node.subscription.block.method;

import java.time.Duration;

public record PollBlockSubscriptionMethodConfigurationProperties(Duration interval)
        implements BlockSubscriptionMethodConfigurationProperties {}
