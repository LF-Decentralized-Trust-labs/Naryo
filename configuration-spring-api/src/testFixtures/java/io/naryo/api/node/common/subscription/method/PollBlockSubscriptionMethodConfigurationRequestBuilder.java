package io.naryo.api.node.common.subscription.method;

import java.time.Duration;

import io.naryo.api.node.common.request.subscription.method.PollBlockSubscriptionConfigurationMethodRequest;
import org.instancio.Instancio;

public final class PollBlockSubscriptionMethodConfigurationRequestBuilder
        extends BlockSubscriptionMethodConfigurationRequestBuilder<
                PollBlockSubscriptionMethodConfigurationRequestBuilder,
                PollBlockSubscriptionConfigurationMethodRequest> {

    private Duration interval;

    @Override
    public PollBlockSubscriptionMethodConfigurationRequestBuilder self() {
        return this;
    }

    @Override
    public PollBlockSubscriptionConfigurationMethodRequest build() {
        return new PollBlockSubscriptionConfigurationMethodRequest(getInterval());
    }

    public PollBlockSubscriptionMethodConfigurationRequestBuilder withInterval(Duration interval) {
        this.interval = interval;
        return self();
    }

    public Duration getInterval() {
        return this.interval == null ? Instancio.create(Duration.class) : this.interval;
    }
}
