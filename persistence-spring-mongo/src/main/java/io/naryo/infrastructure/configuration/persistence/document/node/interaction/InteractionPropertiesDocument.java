package io.naryo.infrastructure.configuration.persistence.document.node.interaction;

import io.naryo.domain.node.interaction.InteractionStrategy;
import lombok.Getter;

@Getter
public abstract class InteractionPropertiesDocument {
    private InteractionStrategy strategy;
}
