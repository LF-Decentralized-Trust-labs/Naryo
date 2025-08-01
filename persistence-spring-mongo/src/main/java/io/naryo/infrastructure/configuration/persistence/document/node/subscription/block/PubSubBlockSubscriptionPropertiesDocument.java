package io.naryo.infrastructure.configuration.persistence.document.node.subscription.block;

import java.math.BigInteger;

import io.naryo.application.configuration.source.model.node.subscription.PubsubBlockSubscriptionDescriptor;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("pub_sub_block_subscription")
public final class PubSubBlockSubscriptionPropertiesDocument
        extends BlockSubscriptionPropertiesDocument implements PubsubBlockSubscriptionDescriptor {

    public PubSubBlockSubscriptionPropertiesDocument(
            BigInteger initialBlock,
            BigInteger confirmationBlocks,
            BigInteger missingTxRetryBlocks,
            BigInteger eventInvalidationBlockThreshold,
            BigInteger replayBlockOffset,
            BigInteger syncBlockLimit) {
        super(
                initialBlock,
                confirmationBlocks,
                missingTxRetryBlocks,
                eventInvalidationBlockThreshold,
                replayBlockOffset,
                syncBlockLimit);
    }
}
