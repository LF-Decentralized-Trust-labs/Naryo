package io.naryo.infrastructure.configuration.provider.node.mapper;

import java.math.BigInteger;
import java.time.Duration;

import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.node.subscription.SubscriptionConfiguration;
import io.naryo.domain.node.subscription.block.BlockSubscriptionConfiguration;
import io.naryo.domain.node.subscription.block.method.poll.Interval;
import io.naryo.domain.node.subscription.block.method.poll.PollBlockSubscriptionMethodConfiguration;
import io.naryo.domain.node.subscription.block.method.pubsub.PubSubBlockSubscriptionMethodConfiguration;
import io.naryo.infrastructure.configuration.persistence.document.node.subscription.SubscriptionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.subscription.block.BlockSubscriptionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.subscription.block.PollBlockSubscriptionDocument;

public class SubscriptionPropertiesDocumentMapper {

    public static SubscriptionConfiguration fromDocument(SubscriptionPropertiesDocument document) {
        if (document instanceof BlockSubscriptionPropertiesDocument) {
            return mapBlockSubscription((BlockSubscriptionPropertiesDocument) document);
        }
        return createDefaultBlockSubscriptionConfiguration();
    }

    private static BlockSubscriptionConfiguration mapBlockSubscription(
            BlockSubscriptionPropertiesDocument document) {
        var method =
                switch (document.getMethod()) {
                    case POLL ->
                            new PollBlockSubscriptionMethodConfiguration(
                                    new Interval(
                                            ((PollBlockSubscriptionDocument) document)
                                                    .getInterval()));
                    case PUBSUB -> new PubSubBlockSubscriptionMethodConfiguration();
                };
        return new BlockSubscriptionConfiguration(
                method,
                document.getInitialBlock(),
                new NonNegativeBlockNumber(BigInteger.ZERO),
                new NonNegativeBlockNumber(document.getConfirmationBlocks()),
                new NonNegativeBlockNumber(document.getMissingTxRetryBlocks()),
                new NonNegativeBlockNumber(document.getEventInvalidationBlockThreshold()),
                new NonNegativeBlockNumber(document.getReplayBlockOffset()),
                new NonNegativeBlockNumber(document.getSyncBlockLimit()));
    }

    private static BlockSubscriptionConfiguration createDefaultBlockSubscriptionConfiguration() {
        return new BlockSubscriptionConfiguration(
                new PollBlockSubscriptionMethodConfiguration(new Interval(Duration.ofSeconds(15))),
                BigInteger.valueOf(-1), // initialBlock
                new NonNegativeBlockNumber(BigInteger.ZERO), // latestBlock
                new NonNegativeBlockNumber(BigInteger.valueOf(12)), // confirmationBlocks
                new NonNegativeBlockNumber(BigInteger.valueOf(200)), // missingTxRetryBlocks
                new NonNegativeBlockNumber(BigInteger.TWO), // eventInvalidationBlockThreshold
                new NonNegativeBlockNumber(BigInteger.valueOf(12)), // replayBlockOffset
                new NonNegativeBlockNumber(BigInteger.valueOf(20000)) // syncBlockLimit
                );
    }
}
