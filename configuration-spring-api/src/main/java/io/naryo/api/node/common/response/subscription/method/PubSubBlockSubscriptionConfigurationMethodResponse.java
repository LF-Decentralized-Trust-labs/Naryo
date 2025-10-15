package io.naryo.api.node.common.response.subscription.method;

import lombok.Builder;

@Builder
public record PubSubBlockSubscriptionConfigurationMethodResponse(String method)
        implements BlockSubscriptionMethodConfigurationResponse {}
