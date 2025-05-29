package io.librevents.application.node.subscription.block.factory;

import io.librevents.application.common.Mapper;
import io.librevents.application.node.calculator.StartBlockCalculator;
import io.librevents.application.node.dispatch.Dispatcher;
import io.librevents.application.node.interactor.block.BlockInteractor;
import io.librevents.application.node.interactor.block.dto.Block;
import io.librevents.application.node.subscription.block.BlockSubscriber;
import io.librevents.application.node.subscription.block.poll.PollBlockSubscriber;
import io.librevents.application.node.subscription.block.pubsub.PubSubBlockSubscriber;
import io.librevents.domain.event.block.BlockEvent;
import io.librevents.domain.node.Node;
import io.librevents.domain.node.subscription.SubscriptionStrategy;
import io.librevents.domain.node.subscription.block.BlockSubscriptionConfiguration;

public final class DefaultBlockSubscriberFactory implements BlockSubscriberFactory {

    private final Mapper<Block, BlockEvent> blockMapper;

    public DefaultBlockSubscriberFactory(Mapper<Block, BlockEvent> blockMapper) {
        this.blockMapper = blockMapper;
    }

    @Override
    public BlockSubscriber create(BlockInteractor interactor, Dispatcher dispatcher, Node node) {
        if (node.getSubscriptionConfiguration()
                .getStrategy()
                .equals(SubscriptionStrategy.BLOCK_BASED)) {
            return switch (((BlockSubscriptionConfiguration) node.getSubscriptionConfiguration())
                    .getMethodConfiguration()
                    .getMethod()) {
                case POLL ->
                        new PollBlockSubscriber(
                                interactor,
                                dispatcher,
                                node,
                                blockMapper,
                                new StartBlockCalculator(node, interactor));
                case PUBSUB ->
                        new PubSubBlockSubscriber(
                                interactor,
                                dispatcher,
                                node,
                                blockMapper,
                                new StartBlockCalculator(node, interactor));
            };
        } else {
            throw new IllegalArgumentException(
                    "Unsupported subscription strategy: "
                            + node.getSubscriptionConfiguration().getStrategy());
        }
    }
}
