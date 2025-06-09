package io.naryo.infrastructure.configuration.source.env.model.node.eth.priv;

import io.naryo.infrastructure.configuration.source.env.model.node.eth.EthNodeVisibilityConfigurationProperties;
import jakarta.validation.constraints.NotBlank;

public record PrivateEthNodeVisibilityConfigurationProperties(
        @NotBlank String groupId, @NotBlank String precompiledAddress)
        implements EthNodeVisibilityConfigurationProperties {}
