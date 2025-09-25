package io.naryo.domain.node.subscription.method;

import io.naryo.domain.node.subscription.block.method.poll.Interval;
import io.naryo.domain.node.subscription.block.method.poll.PollBlockSubscriptionMethodConfiguration;
import org.instancio.Instancio;

public final class PollBlockSubscriptionMethodConfigurationBuilder
        extends BlockSubscriptionMethodConfigurationBuilder<
                PollBlockSubscriptionMethodConfigurationBuilder,
                PollBlockSubscriptionMethodConfiguration> {

    private Interval interval;

    @Override
    public PollBlockSubscriptionMethodConfigurationBuilder self() {
        return this;
    }

    @Override
    public PollBlockSubscriptionMethodConfiguration build() {
        return new PollBlockSubscriptionMethodConfiguration(getInterval());
    }

    public PollBlockSubscriptionMethodConfigurationBuilder withInterval(Interval interval) {
        this.interval = interval;
        return self();
    }

    public Interval getInterval() {
        return this.interval == null ? Instancio.create(Interval.class) : this.interval;
    }
}
