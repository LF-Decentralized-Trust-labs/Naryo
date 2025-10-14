package io.naryo.api.node.common.response.subscription.method;

import java.time.Duration;

public final class PollBlockSubscriptionConfigurationMethodResponse
        extends BlockSubscriptionMethodConfigurationResponse {

    private Duration interval;

    public PollBlockSubscriptionConfigurationMethodResponse(String method, Duration interval) {
        super(method);
        this.interval = interval;
    }
}
