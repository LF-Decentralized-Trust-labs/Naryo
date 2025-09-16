package io.naryo.infrastructure.configuration.persistence.document.node.subscription.block;

import java.math.BigInteger;
import java.time.Duration;
import java.util.Optional;

import io.naryo.application.configuration.source.model.node.subscription.PollBlockSubscriptionDescriptor;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("poll_block_subscription")
public final class PollBlockSubscriptionPropertiesDocument
        extends BlockSubscriptionPropertiesDocument implements PollBlockSubscriptionDescriptor {

    private @Setter @NotNull Duration interval;

    public PollBlockSubscriptionPropertiesDocument(
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
        this.interval = interval;
    }

    @Override
    public Optional<Duration> getInterval() {
        return Optional.ofNullable(interval);
    }
}
