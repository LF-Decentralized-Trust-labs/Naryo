package io.naryo.api.node.common.request.subscription.method;

import java.time.Duration;

import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethodConfiguration;
import io.naryo.domain.node.subscription.block.method.poll.Interval;
import io.naryo.domain.node.subscription.block.method.poll.PollBlockSubscriptionMethodConfiguration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Poll block subscription request")
@Getter
public final class PollBlockSubscriptionConfigurationMethodRequest
        extends BlockSubscriptionMethodConfigurationRequest {

    public static final Duration DEFAULT_INTERVAL = Duration.ofSeconds(5);

    @Schema(defaultValue = "PT5S", description = "ISO 8601 duration, must be positive")
    private final Duration interval;

    public PollBlockSubscriptionConfigurationMethodRequest(Duration interval) {
        this.interval = interval;
    }

    @Override
    public BlockSubscriptionMethodConfiguration toDomain() {
        Duration resolvedInterval = interval != null ? interval : DEFAULT_INTERVAL;
        return new PollBlockSubscriptionMethodConfiguration(new Interval(resolvedInterval));
    }
}
