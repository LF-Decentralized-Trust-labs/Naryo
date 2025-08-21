package io.naryo.infrastructure.configuration.persistence.entity.node.subscription.block;

import java.math.BigInteger;
import java.time.Duration;
import java.util.Optional;

import io.naryo.application.configuration.source.model.node.subscription.PollBlockSubscriptionDescriptor;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("poll")
@NoArgsConstructor
public class PollBlockSubscriptionEntity extends BlockSubscriptionEntity
        implements PollBlockSubscriptionDescriptor {

    private static final Duration DEFAULT_INTERVAL = Duration.ofSeconds(5);

    private @Column(name = "interval") @Setter @NotNull Duration interval;

    public PollBlockSubscriptionEntity(
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
        return Optional.ofNullable(this.interval);
    }
}
