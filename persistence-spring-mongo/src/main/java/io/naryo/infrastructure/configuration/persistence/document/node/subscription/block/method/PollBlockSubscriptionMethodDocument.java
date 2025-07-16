package io.naryo.infrastructure.configuration.persistence.document.node.subscription.block.method;

import java.time.Duration;

import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@Getter
@TypeAlias("poll_block_subscription_method")
public class PollBlockSubscriptionMethodDocument extends BlockSubscriptionMethodDocument {
    private Duration interval;
}
