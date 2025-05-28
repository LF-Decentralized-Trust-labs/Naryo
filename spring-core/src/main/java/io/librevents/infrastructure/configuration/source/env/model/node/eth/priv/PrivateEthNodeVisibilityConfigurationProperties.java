package io.librevents.infrastructure.configuration.source.env.model.node.eth.priv;

import io.librevents.infrastructure.configuration.source.env.model.node.eth.EthNodeVisibilityConfigurationProperties;

public record PrivateEthNodeVisibilityConfigurationProperties(
        String groupId, String precompiledAddress)
        implements EthNodeVisibilityConfigurationProperties {}
