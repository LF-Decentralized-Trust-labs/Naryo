package io.librevents.infrastructure.configuration.node;

import io.librevents.infrastructure.configuration.node.connection.ConnectionProperties;
import io.librevents.infrastructure.configuration.node.interaction.InteractionProperties;
import io.librevents.infrastructure.configuration.node.subscription.SubscriptionProperties;

public record NodeProperties(
        String name,
        SubscriptionProperties subscription,
        InteractionProperties interaction,
        ConnectionProperties connection) {}
