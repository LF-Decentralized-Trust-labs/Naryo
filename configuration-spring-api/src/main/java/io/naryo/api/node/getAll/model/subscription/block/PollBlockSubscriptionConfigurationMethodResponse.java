package io.naryo.api.node.getAll.model.subscription.block;

import java.time.Duration;

import io.naryo.domain.node.subscription.block.method.poll.PollBlockSubscriptionMethodConfiguration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Poll block subscription method")
@Getter
public final class PollBlockSubscriptionConfigurationMethodResponse
        extends BlockSubscriptionMethodConfigurationResponse {

    private final Duration interval;

    public PollBlockSubscriptionConfigurationMethodResponse(Duration interval) {
        this.interval = interval;
    }

    public static PollBlockSubscriptionConfigurationMethodResponse fromDomain(
            PollBlockSubscriptionMethodConfiguration pollBlockSubscriptionMethodConfiguration) {
        return new PollBlockSubscriptionConfigurationMethodResponse(
                pollBlockSubscriptionMethodConfiguration.getInterval().value());
    }
}
