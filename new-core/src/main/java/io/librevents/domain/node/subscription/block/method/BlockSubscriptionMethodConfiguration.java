package io.librevents.domain.node.subscription.block.method;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public abstract class BlockSubscriptionMethodConfiguration {

    private final BlockSubscriptionMethod method;

    protected BlockSubscriptionMethodConfiguration(BlockSubscriptionMethod method) {
        this.method = method;
    }
}
