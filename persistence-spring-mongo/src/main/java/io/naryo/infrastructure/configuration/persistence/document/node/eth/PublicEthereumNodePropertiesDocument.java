package io.naryo.infrastructure.configuration.persistence.document.node.eth;

import io.naryo.application.configuration.source.model.node.PublicEthereumNodeDescriptor;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("public_ethereum_node")
public class PublicEthereumNodePropertiesDocument extends EthereumNodePropertiesDocument
        implements PublicEthereumNodeDescriptor {}
