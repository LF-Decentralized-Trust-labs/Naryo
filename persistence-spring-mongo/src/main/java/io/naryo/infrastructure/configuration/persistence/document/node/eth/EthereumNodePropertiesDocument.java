package io.naryo.infrastructure.configuration.persistence.document.node.eth;

import io.naryo.infrastructure.configuration.persistence.document.node.NodePropertiesDocument;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("ethereum_node")
@Getter
public class EthereumNodePropertiesDocument extends NodePropertiesDocument {
    private EthereumNodeVisibilityConfigurationDocument configuration;
}
