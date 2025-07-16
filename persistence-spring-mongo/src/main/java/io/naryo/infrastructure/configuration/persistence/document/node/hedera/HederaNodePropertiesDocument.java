package io.naryo.infrastructure.configuration.persistence.document.node.hedera;

import io.naryo.infrastructure.configuration.persistence.document.node.NodePropertiesDocument;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("hedera_node")
public class HederaNodePropertiesDocument extends NodePropertiesDocument {}
