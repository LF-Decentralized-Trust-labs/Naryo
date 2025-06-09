package io.naryo.infrastructure.configuration.source.env.model.node;

import java.util.UUID;

import io.naryo.domain.node.NodeType;
import io.naryo.infrastructure.configuration.source.env.model.node.connection.ConnectionProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.interaction.InteractionProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.subscription.SubscriptionProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NodeProperties(
        @NotNull UUID id,
        @NotBlank String name,
        @NotNull NodeType type,
        @Valid @NotNull SubscriptionProperties subscription,
        @Valid @NotNull InteractionProperties interaction,
        @Valid @NotNull ConnectionProperties connection,
        @Valid @NotNull NodeConfigurationProperties configuration) {}
