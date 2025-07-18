package io.naryo.infrastructure.configuration.persistence.document.node.hedera;

import io.naryo.application.configuration.source.model.node.HederaNodeDescriptor;
import io.naryo.infrastructure.configuration.persistence.document.node.NodePropertiesDocument;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("hedera_node")
public final class HederaNodePropertiesDocument extends NodePropertiesDocument
        implements HederaNodeDescriptor {}
