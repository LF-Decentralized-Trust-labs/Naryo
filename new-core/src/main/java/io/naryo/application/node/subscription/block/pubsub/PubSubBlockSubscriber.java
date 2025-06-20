package io.naryo.application.node.subscription.block.pubsub;

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
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class PubSubBlockSubscriber extends BlockSubscriber {

    private final Consumer<Block> onNext =
            block -> {
                log.info("Processing block {}", block.number());
                dispatcher.dispatch(blockMapper.map(block, Map.of("nodeId", node.getId())));
            };

    private final Consumer<Throwable> onError =
            throwable -> {
                log.error("Error processing block", throwable);
                throw new RuntimeException(throwable);
            };

    public PubSubBlockSubscriber(
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
                () -> {
                    CompositeDisposable compositeDisposable = new CompositeDisposable();
                    compositeDisposable.add(
                            interactor
                                    .replayPastBlocks(calculator.getStartBlock())
                                    .doOnComplete(
                                            () -> compositeDisposable.add(subscribeNewBlocks()))
                                    .subscribe(onNext, onError));
                    return compositeDisposable;
                });
    }

    private Disposable subscribeNewBlocks() throws IOException {
        return interactor.replyFutureBlocks().subscribe(onNext, onError);
    }
}
