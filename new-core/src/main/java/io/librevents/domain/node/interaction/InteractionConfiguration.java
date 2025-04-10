package io.librevents.domain.node.interaction;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public abstract class InteractionConfiguration {
    private final InteractionStrategy strategy;

    protected InteractionConfiguration(InteractionStrategy strategy) {
        this.strategy = strategy;
    }
}
