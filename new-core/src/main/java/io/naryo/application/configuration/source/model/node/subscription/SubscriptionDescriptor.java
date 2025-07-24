package io.naryo.application.configuration.source.model.node.subscription;

import io.naryo.application.configuration.source.model.MergeableDescriptor;
import io.naryo.domain.node.subscription.SubscriptionStrategy;

public interface SubscriptionDescriptor extends MergeableDescriptor<SubscriptionDescriptor> {

    SubscriptionStrategy getStrategy();

    default SubscriptionDescriptor merge(SubscriptionDescriptor other) {
        return this;
    }
}
