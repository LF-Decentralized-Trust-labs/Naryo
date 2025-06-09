package io.naryo.infrastructure.configuration.source.env.model.node.eth;

import io.naryo.domain.node.ethereum.EthereumNodeVisibility;
import io.naryo.infrastructure.configuration.source.env.model.node.NodeConfigurationProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record EthereumNodeConfigurationProperties(
        @NotNull EthereumNodeVisibility visibility,
        @Valid @NotNull EthNodeVisibilityConfigurationProperties configuration)
        implements NodeConfigurationProperties {}
