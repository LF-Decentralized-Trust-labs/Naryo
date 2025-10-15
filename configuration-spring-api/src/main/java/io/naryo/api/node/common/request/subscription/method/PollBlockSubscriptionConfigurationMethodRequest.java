package io.naryo.api.node.common.request.subscription.method;

import java.time.Duration;

import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethod;
import lombok.Getter;

@Getter
public final class PollBlockSubscriptionConfigurationMethodRequest
        extends BlockSubscriptionMethodConfigurationRequest {

    private final Duration interval;

    PollBlockSubscriptionConfigurationMethodRequest(Duration interval) {
        super(BlockSubscriptionMethod.POLL);
        this.interval = interval;
    }
}
