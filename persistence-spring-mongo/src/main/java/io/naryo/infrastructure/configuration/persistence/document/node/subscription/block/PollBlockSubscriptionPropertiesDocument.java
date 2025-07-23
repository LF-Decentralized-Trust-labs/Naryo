package io.naryo.infrastructure.configuration.persistence.document.node.subscription.block;

import java.math.BigInteger;
import java.time.Duration;
import java.util.Optional;

import io.naryo.application.configuration.source.model.node.subscription.PollBlockSubscriptionDescriptor;
import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@TypeAlias("poll_block_subscription")
public final class PollBlockSubscriptionPropertiesDocument
        extends BlockSubscriptionPropertiesDocument implements PollBlockSubscriptionDescriptor {

    private static final Duration DEFAULT_INTERVAL = Duration.ofSeconds(5);

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
                BlockSubscriptionMethod.POLL,
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
