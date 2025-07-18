package io.naryo.infrastructure.configuration.persistence.document.node.subscription.block;

import java.time.Duration;

import io.naryo.application.configuration.source.model.node.subscription.PollBlockSubscriptionDescriptor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("poll_block_subscription")
@Getter
@Setter
public final class PollBlockSubscriptionDocument extends BlockSubscriptionPropertiesDocument
        implements PollBlockSubscriptionDescriptor {
    private Duration interval;
}
