package io.librevents.application.node.subscription.block;

import java.util.Objects;

import io.librevents.application.common.Mapper;
import io.librevents.application.node.calculator.StartBlockCalculator;
import io.librevents.application.node.dispatch.Dispatcher;
import io.librevents.application.node.interactor.block.BlockInteractor;
import io.librevents.application.node.interactor.block.dto.Block;
import io.librevents.application.node.subscription.block.poll.PollBlockSubscriber;
import io.librevents.application.node.subscription.block.pubsub.PubSubBlockSubscriber;
import io.librevents.domain.event.block.BlockEvent;
import io.librevents.domain.node.Node;
import io.librevents.domain.node.subscription.block.method.BlockSubscriptionMethod;

public final class BlockSubscriberFactory {

    private final BlockInteractor interactor;
    private final Dispatcher dispatcher;
    private final Mapper<Block, BlockEvent> blockMapper;
    private final StartBlockCalculator calculator;

    public BlockSubscriberFactory(
            BlockInteractor interactor,
            Dispatcher dispatcher,
            Mapper<Block, BlockEvent> blockMapper,
            StartBlockCalculator calculator) {
        Objects.requireNonNull(interactor, "interactor must not be null");
        Objects.requireNonNull(dispatcher, "dispatcher must not be null");
        Objects.requireNonNull(blockMapper, "blockMapper must not be null");
        Objects.requireNonNull(calculator, "calculator must not be null");
        this.interactor = interactor;
        this.dispatcher = dispatcher;
        this.blockMapper = blockMapper;
        this.calculator = calculator;
    }

    public BlockSubscriber create(BlockSubscriptionMethod method, Node node) {
        Objects.requireNonNull(method, "method must not be null");
        Objects.requireNonNull(node, "node must not be null");
        return switch (method) {
            case POLL ->
                    new PollBlockSubscriber(interactor, dispatcher, node, blockMapper, calculator);
            case PUBSUB ->
                    new PubSubBlockSubscriber(
                            interactor, dispatcher, node, blockMapper, calculator);
        };
    }
}
