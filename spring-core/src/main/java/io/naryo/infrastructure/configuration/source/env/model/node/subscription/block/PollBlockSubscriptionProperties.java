package io.naryo.infrastructure.configuration.source.env.model.node.subscription.block;

import java.math.BigInteger;
import java.time.Duration;
import java.util.Optional;

import io.naryo.application.configuration.source.model.node.subscription.PollBlockSubscriptionDescriptor;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;

public final class PollBlockSubscriptionProperties extends BlockSubscriptionProperties
        implements PollBlockSubscriptionDescriptor {

    private static final Duration DEFAULT_INTERVAL = Duration.ofSeconds(5);

    private @Setter @NotNull Duration interval;

    public PollBlockSubscriptionProperties(
            BigInteger initialBlock,
            BigInteger confirmationBlocks,
            BigInteger missingTxRetryBlocks,
            BigInteger eventInvalidationBlockThreshold,
            BigInteger replayBlockOffset,
            BigInteger syncBlockLimit,
            Duration interval) {
        super(
                initialBlock,
                confirmationBlocks,
                missingTxRetryBlocks,
                eventInvalidationBlockThreshold,
                replayBlockOffset,
                syncBlockLimit);
        this.interval = interval != null ? interval : DEFAULT_INTERVAL;
    }

    @Override
    public Optional<Duration> getInterval() {
        return Optional.ofNullable(interval);
    }
}
