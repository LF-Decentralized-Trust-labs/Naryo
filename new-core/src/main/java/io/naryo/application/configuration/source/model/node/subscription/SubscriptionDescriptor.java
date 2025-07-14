package io.naryo.application.configuration.source.model.node.subscription;

import io.naryo.application.configuration.source.model.MergeableDescriptor;
import io.naryo.domain.node.subscription.SubscriptionStrategy;

public interface SubscriptionDescriptor extends MergeableDescriptor<SubscriptionDescriptor> {

    SubscriptionStrategy strategy();

    default SubscriptionDescriptor merge(SubscriptionDescriptor descriptor) {
        if (descriptor == null) {
            return this;
        }

        if (!this.strategy().equals(descriptor.strategy())) {
            return descriptor;
        }

        return this;
    }
}
