package io.naryo.application.node.subscription.block.factory;

import io.naryo.application.common.Mapper;
import io.naryo.application.node.calculator.StartBlockCalculator;
import io.naryo.application.node.dispatch.Dispatcher;
import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.application.node.interactor.block.dto.Block;
import io.naryo.application.node.subscription.block.BlockSubscriber;
import io.naryo.application.node.subscription.block.poll.PollBlockSubscriber;
import io.naryo.application.node.subscription.block.pubsub.PubSubBlockSubscriber;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.subscription.SubscriptionStrategy;
import io.naryo.domain.node.subscription.block.BlockSubscriptionConfiguration;

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
