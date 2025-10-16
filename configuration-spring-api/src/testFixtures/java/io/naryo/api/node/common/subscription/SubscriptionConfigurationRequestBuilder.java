package io.naryo.api.node.common.subscription;

import io.naryo.api.node.RequestBuilder;
import io.naryo.api.node.common.request.subscription.SubscriptionConfigurationRequest;

public abstract class SubscriptionConfigurationRequestBuilder<
                T extends SubscriptionConfigurationRequestBuilder<T, Y>,
                Y extends SubscriptionConfigurationRequest>
        implements RequestBuilder<T, Y> {}
