package io.naryo.api.node.common.request.subscription.method;

import java.time.Duration;

import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethod;
import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethodConfiguration;
import io.naryo.domain.node.subscription.block.method.poll.Interval;
import io.naryo.domain.node.subscription.block.method.poll.PollBlockSubscriptionMethodConfiguration;
import lombok.Getter;

@Getter
public final class PollBlockSubscriptionConfigurationMethodRequest
        extends BlockSubscriptionMethodConfigurationRequest {

    private final Duration interval;

    public PollBlockSubscriptionConfigurationMethodRequest(Duration interval) {
        super(BlockSubscriptionMethod.POLL);
        this.interval = interval;
    }

    @Override
    public BlockSubscriptionMethodConfiguration toDomain() {
        return new PollBlockSubscriptionMethodConfiguration(new Interval(this.interval));
    }
}
