package io.librevents.infrastructure.configuration.source.env.model.node.eth;

import io.librevents.domain.node.ethereum.EthereumNodeVisibility;
import io.librevents.infrastructure.configuration.source.env.model.node.NodeConfigurationProperties;

public record EthereumNodeConfigurationProperties(
        EthereumNodeVisibility visibility, EthNodeVisibilityConfigurationProperties configuration)
        implements NodeConfigurationProperties {}
