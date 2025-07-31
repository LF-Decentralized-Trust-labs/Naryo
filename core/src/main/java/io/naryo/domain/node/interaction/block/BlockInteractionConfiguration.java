package io.naryo.domain.node.interaction.block;

import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.interaction.InteractionStrategy;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class BlockInteractionConfiguration extends InteractionConfiguration {

    private final InteractionMode mode;

    protected BlockInteractionConfiguration(InteractionMode mode) {
        super(InteractionStrategy.BLOCK_BASED);
        this.mode = mode;
    }
}
