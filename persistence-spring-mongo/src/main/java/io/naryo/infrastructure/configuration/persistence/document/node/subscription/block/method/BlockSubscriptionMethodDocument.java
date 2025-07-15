package io.naryo.infrastructure.configuration.persistence.document.node.subscription.block.method;

import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethod;
import lombok.Getter;

@Getter
public abstract class BlockSubscriptionMethodDocument {
    private BlockSubscriptionMethod method;
}
