package io.librevents.application.node.subscription.block;

import java.util.Objects;

import io.librevents.application.common.Mapper;
import io.librevents.application.node.calculator.StartBlockCalculator;
import io.librevents.application.node.dispatch.Dispatcher;
import io.librevents.application.node.interactor.block.BlockInteractor;
import io.librevents.application.node.interactor.block.dto.Block;
import io.librevents.application.node.subscription.Subscriber;
import io.librevents.domain.event.block.BlockEvent;
import io.librevents.domain.node.Node;

public abstract class BlockSubscriber implements Subscriber {

    protected final BlockInteractor interactor;
    protected final Dispatcher dispatcher;
    protected final Node node;
    protected final Mapper<Block, BlockEvent> blockMapper;
    protected final StartBlockCalculator calculator;

    protected BlockSubscriber(
            BlockInteractor interactor,
            Dispatcher dispatcher,
            Node node,
            Mapper<Block, BlockEvent> blockMapper,
            StartBlockCalculator startBlockCalculator) {
        Objects.requireNonNull(interactor, "interactor cannot be null");
        Objects.requireNonNull(dispatcher, "dispatcher cannot be null");
        Objects.requireNonNull(node, "node cannot be null");
        Objects.requireNonNull(blockMapper, "blockMapper cannot be null");
        Objects.requireNonNull(startBlockCalculator, "startBlockCalculator cannot be null");
        this.interactor = interactor;
        this.dispatcher = dispatcher;
        this.node = node;
        this.blockMapper = blockMapper;
        this.calculator = startBlockCalculator;
    }
}
