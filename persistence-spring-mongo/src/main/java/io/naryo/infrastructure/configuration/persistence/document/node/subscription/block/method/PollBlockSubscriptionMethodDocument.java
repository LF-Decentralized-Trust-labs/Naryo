package io.naryo.infrastructure.configuration.persistence.document.node.subscription.block.method;

import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

import java.time.Duration;

@Getter
@TypeAlias("poll_block_subscription_method")
public class PollBlockSubscriptionMethodDocument extends BlockSubscriptionMethodDocument {
    private Duration interval;
}
