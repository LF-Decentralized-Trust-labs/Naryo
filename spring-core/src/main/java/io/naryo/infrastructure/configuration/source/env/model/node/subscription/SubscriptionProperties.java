package io.naryo.infrastructure.configuration.source.env.model.node.subscription;

import io.naryo.application.configuration.source.model.node.subscription.SubscriptionDescriptor;
import io.naryo.domain.node.subscription.SubscriptionStrategy;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public abstract class SubscriptionProperties implements SubscriptionDescriptor {}

