package io.naryo.infrastructure.configuration.persistence.document.node.subscription.block.method;

import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@Getter
@TypeAlias("pub_sub_block_subscription_method")
public class PubSubBlockSubscriptionMethodDocument extends BlockSubscriptionMethodDocument {}
