package io.naryo.infrastructure.configuration.persistence.document.node.subscription.block;

import io.naryo.application.configuration.source.model.node.subscription.PubsubBlockSubscriptionDescriptor;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("pub_sub_block_subscription")
public final class PubSubBlockSubscriptionDocument extends BlockSubscriptionPropertiesDocument
        implements PubsubBlockSubscriptionDescriptor {}
