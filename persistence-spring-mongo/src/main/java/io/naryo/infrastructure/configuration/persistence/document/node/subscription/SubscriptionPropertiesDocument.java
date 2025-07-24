package io.naryo.infrastructure.configuration.persistence.document.node.subscription;

import io.naryo.application.configuration.source.model.node.subscription.SubscriptionDescriptor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public abstract class SubscriptionPropertiesDocument implements SubscriptionDescriptor {}
