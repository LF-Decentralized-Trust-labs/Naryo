package io.naryo.api.node.common.subscription.method;

import io.naryo.api.node.RequestBuilder;
import io.naryo.api.node.common.request.subscription.method.BlockSubscriptionMethodConfigurationRequest;

public abstract class BlockSubscriptionMethodConfigurationRequestBuilder<
                T extends BlockSubscriptionMethodConfigurationRequestBuilder<T, Y>,
                Y extends BlockSubscriptionMethodConfigurationRequest>
        implements RequestBuilder<T, Y> {}
