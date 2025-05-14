package io.librevents.infrastructure.configuration.source.env.model.node;

import io.librevents.infrastructure.configuration.source.env.model.node.connection.ConnectionProperties;
import io.librevents.infrastructure.configuration.source.env.model.node.interaction.InteractionProperties;
import io.librevents.infrastructure.configuration.source.env.model.node.subscription.SubscriptionProperties;

public record NodeProperties(
        String name,
        SubscriptionProperties subscription,
        InteractionProperties interaction,
        ConnectionProperties connection) {}
