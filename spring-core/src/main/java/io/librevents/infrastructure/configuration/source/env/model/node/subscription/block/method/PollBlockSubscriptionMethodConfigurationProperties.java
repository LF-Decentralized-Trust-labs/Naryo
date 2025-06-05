package io.librevents.infrastructure.configuration.source.env.model.node.subscription.block.method;

import java.time.Duration;

import jakarta.validation.constraints.NotNull;

public record PollBlockSubscriptionMethodConfigurationProperties(@NotNull Duration interval)
        implements BlockSubscriptionMethodConfigurationProperties {

    private static final Duration DEFAULT_INTERVAL = Duration.ofSeconds(5);

    public PollBlockSubscriptionMethodConfigurationProperties(Duration interval) {
        this.interval = interval != null ? interval : DEFAULT_INTERVAL;
    }

    public PollBlockSubscriptionMethodConfigurationProperties() {
        this(DEFAULT_INTERVAL);
    }
}
