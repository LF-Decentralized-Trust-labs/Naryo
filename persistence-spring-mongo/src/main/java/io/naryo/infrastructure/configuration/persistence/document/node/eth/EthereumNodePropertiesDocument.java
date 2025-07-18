package io.naryo.infrastructure.configuration.persistence.document.node.eth;

import io.naryo.domain.node.ethereum.EthereumNodeVisibility;
import io.naryo.infrastructure.configuration.persistence.document.node.NodePropertiesDocument;
import lombok.Getter;

@Getter
public abstract class EthereumNodePropertiesDocument extends NodePropertiesDocument {
    private EthereumNodeVisibility visibility;
}
