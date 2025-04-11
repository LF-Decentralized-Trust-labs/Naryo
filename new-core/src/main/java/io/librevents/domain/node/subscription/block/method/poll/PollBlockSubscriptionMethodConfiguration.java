package io.librevents.domain.node.subscription.block.method.poll;

import java.util.Objects;

import io.librevents.domain.node.subscription.block.method.BlockSubscriptionMethod;
import io.librevents.domain.node.subscription.block.method.BlockSubscriptionMethodConfiguration;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class PollBlockSubscriptionMethodConfiguration
        extends BlockSubscriptionMethodConfiguration {

    private final Interval interval;

    public PollBlockSubscriptionMethodConfiguration(Interval interval) {
        super(BlockSubscriptionMethod.POLL);
        Objects.requireNonNull(interval, "interval cannot be null");
        this.interval = interval;
    }
}
