package io.naryo.application.node.subscription.block.poll;

import java.io.IOException;
import java.util.Map;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import io.naryo.application.common.Mapper;
import io.naryo.application.node.calculator.StartBlockCalculator;
import io.naryo.application.node.dispatch.Dispatcher;
import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.application.node.interactor.block.dto.Block;
import io.naryo.application.node.subscription.block.BlockSubscriber;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.domain.node.Node;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class PollBlockSubscriber extends BlockSubscriber {

    public PollBlockSubscriber(
            BlockInteractor interactor,
            Dispatcher dispatcher,
            Node node,
            Mapper<Block, BlockEvent> blockMapper,
            StartBlockCalculator calculator,
            CircuitBreaker circuitBreaker,
            Retry retry) {
        super(interactor, dispatcher, node, blockMapper, calculator, circuitBreaker, retry);
    }

    @Override
    public Disposable subscribe() throws IOException {
        log.info("Subscribing to block for node {}", node.getId());
        return applyCircuitBreaker(
                () ->
                        interactor
                                .replayPastAndFutureBlocks(calculator.getStartBlock())
                                .subscribe(
                                        block -> {
                                            log.info("Processing block {}", block.number());
                                            dispatcher.dispatch(
                                                    blockMapper.map(
                                                            block, Map.of("nodeId", node.getId())));
                                        },
                                        throwable -> {
                                            log.error("Error processing block", throwable);
                                            throw new RuntimeException(throwable);
                                        }));
    }
}
