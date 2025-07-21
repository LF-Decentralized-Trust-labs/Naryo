package io.naryo.application.configuration.source.model.node.subscription;

import io.naryo.application.configuration.source.model.MergeableDescriptor;
import io.naryo.domain.node.subscription.SubscriptionStrategy;

public interface SubscriptionDescriptor extends MergeableDescriptor<SubscriptionDescriptor> {

    SubscriptionStrategy getStrategy();

    default SubscriptionDescriptor merge(SubscriptionDescriptor descriptor) {
        if (descriptor == null) {
            return this;
        }

        if (!this.getStrategy().equals(descriptor.getStrategy())) {
            return descriptor;
        }

        return this;
    }
}
