package io.naryo.application.node.subscription.block;

import java.util.Objects;
import java.util.concurrent.Callable;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.retry.Retry;
import io.naryo.application.common.Mapper;
import io.naryo.application.node.calculator.StartBlockCalculator;
import io.naryo.application.node.dispatch.Dispatcher;
import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.application.node.interactor.block.dto.Block;
import io.naryo.application.node.subscription.Subscriber;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.domain.node.Node;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BlockSubscriber implements Subscriber {

    protected final BlockInteractor interactor;
    protected final Dispatcher dispatcher;
    protected final Node node;
    protected final Mapper<Block, BlockEvent> blockMapper;
    protected final StartBlockCalculator calculator;
    protected final CircuitBreaker circuitBreaker;
    protected final Retry retry;

    protected BlockSubscriber(
            BlockInteractor interactor,
            Dispatcher dispatcher,
            Node node,
            Mapper<Block, BlockEvent> blockMapper,
            StartBlockCalculator startBlockCalculator,
            CircuitBreaker circuitBreaker,
            Retry retry) {
        Objects.requireNonNull(interactor, "interactor cannot be null");
        Objects.requireNonNull(dispatcher, "dispatcher cannot be null");
        Objects.requireNonNull(node, "node cannot be null");
        Objects.requireNonNull(blockMapper, "blockMapper cannot be null");
        Objects.requireNonNull(startBlockCalculator, "startBlockCalculator cannot be null");
        Objects.requireNonNull(circuitBreaker, "circuitBreaker cannot be null");
        Objects.requireNonNull(retry, "retry cannot be null");
        this.interactor = interactor;
        this.dispatcher = dispatcher;
        this.node = node;
        this.blockMapper = blockMapper;
        this.calculator = startBlockCalculator;
        this.circuitBreaker = circuitBreaker;
        this.retry = retry;
    }

    public Disposable applyCircuitBreaker(Callable<Disposable> callable) {
        var decorated =
                Decorators.ofSupplier(
                                () -> {
                                    try {
                                        return callable.call();
                                    } catch (Exception e) {
                                        log.warn(
                                                "Block subscription failed, will be retried if policy allows. Reason: {}",
                                                e.getMessage());
                                        throw new RuntimeException(e);
                                    }
                                })
                        .withCircuitBreaker(circuitBreaker)
                        .withRetry(retry)
                        .decorate();

        try {
            return decorated.get();
        } catch (Exception e) {
            log.error("Subscription for block {} failed after retries", node.getId(), e);
            throw new RuntimeException("Could not subscribe to block stream", e);
        }
    }
}
