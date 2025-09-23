package io.naryo.domain.node.interaction.block;

import io.naryo.domain.node.interaction.InteractionConfigurationBuilder;

public abstract class BlockInteractionConfigurationBuilder<
                T extends BlockInteractionConfigurationBuilder<T, Y>,
                Y extends BlockInteractionConfiguration>
        extends InteractionConfigurationBuilder<T, Y> {}
