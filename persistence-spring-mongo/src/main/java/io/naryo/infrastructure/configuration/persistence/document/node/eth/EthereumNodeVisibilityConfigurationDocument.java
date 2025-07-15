package io.naryo.infrastructure.configuration.persistence.document.node.eth;

import io.naryo.domain.node.ethereum.EthereumNodeVisibility;
import lombok.Getter;

@Getter
public abstract class EthereumNodeVisibilityConfigurationDocument {
    private EthereumNodeVisibility visibility;
}
