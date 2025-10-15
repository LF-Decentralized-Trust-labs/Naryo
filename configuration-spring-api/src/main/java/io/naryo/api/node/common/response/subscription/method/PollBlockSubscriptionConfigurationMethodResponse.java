package io.naryo.api.node.common.response.subscription.method;

import java.time.Duration;

import lombok.Builder;

@Builder
public record PollBlockSubscriptionConfigurationMethodResponse(String method, Duration interval)
        implements BlockSubscriptionMethodConfigurationResponse {}
