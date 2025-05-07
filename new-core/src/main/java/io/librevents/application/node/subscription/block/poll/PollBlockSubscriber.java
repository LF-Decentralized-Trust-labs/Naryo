package io.librevents.application.node.subscription.block.poll;

import java.io.IOException;
import java.util.Map;

import io.librevents.application.common.Mapper;
import io.librevents.application.node.calculator.StartBlockCalculator;
import io.librevents.application.node.dispatch.Dispatcher;
import io.librevents.application.node.interactor.block.BlockInteractor;
import io.librevents.application.node.interactor.block.dto.Block;
import io.librevents.application.node.subscription.block.BlockSubscriber;
import io.librevents.domain.event.block.BlockEvent;
import io.librevents.domain.node.Node;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class PollBlockSubscriber extends BlockSubscriber {

    public PollBlockSubscriber(
            BlockInteractor interactor,
            Dispatcher dispatcher,
            Node node,
            Mapper<Block, BlockEvent> blockMapper,
            StartBlockCalculator calculator) {
        super(interactor, dispatcher, node, blockMapper, calculator);
    }

    @Override
    public Disposable subscribe() throws IOException {
        return interactor
                .replayPastAndFutureBlocks(calculator.getStartBlock())
                .subscribe(
                        block -> {
                            log.info("Processing block {}", block.number());
                            dispatcher.dispatch(
                                    blockMapper.map(block, Map.of("nodeId", node.getId())));
                        },
                        throwable -> {
                            log.error("Error processing block", throwable);
                            // TODO: Handle error
                        });
    }
}
