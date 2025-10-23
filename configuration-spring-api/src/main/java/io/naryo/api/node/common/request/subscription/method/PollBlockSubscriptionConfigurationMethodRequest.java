package io.naryo.api.node.common.request.subscription.method;

import java.time.Duration;

import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethod;
import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethodConfiguration;
import io.naryo.domain.node.subscription.block.method.poll.Interval;
import io.naryo.domain.node.subscription.block.method.poll.PollBlockSubscriptionMethodConfiguration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Poll block subscription")
@Getter
public final class PollBlockSubscriptionConfigurationMethodRequest
        extends BlockSubscriptionMethodConfigurationRequest {

    private final Duration interval;

    public PollBlockSubscriptionConfigurationMethodRequest(Duration interval) {
        this.interval = interval;
    }

    @Override
    public BlockSubscriptionMethodConfiguration toDomain() {
        return new PollBlockSubscriptionMethodConfiguration(new Interval(this.interval));
    }
}
