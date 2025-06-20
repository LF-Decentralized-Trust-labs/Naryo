package io.naryo.application.node.subscription.block.poll;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import io.naryo.application.node.calculator.StartBlockCalculator;
import io.naryo.application.node.dispatch.Dispatcher;
import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.application.node.interactor.block.dto.Block;
import io.naryo.application.node.interactor.block.mapper.BlockToBlockEventMapper;
import io.naryo.application.node.subscription.block.BlockSubscriber;
import io.naryo.application.node.subscription.block.BlockSubscriberTest;
import io.naryo.domain.node.Node;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PollBlockSubscriberTest extends BlockSubscriberTest {

    @Override
    protected BlockSubscriber createBlockSubscriber(
            BlockInteractor interactor,
            Dispatcher dispatcher,
            Node node,
            BlockToBlockEventMapper blockMapper,
            StartBlockCalculator calculator,
            CircuitBreaker circuitBreaker,
            Retry retry) {
        return new PollBlockSubscriber(
                interactor, dispatcher, node, blockMapper, calculator, circuitBreaker, retry);
    }

    @Test
    void testSubscribe() throws IOException {
        when(calculator.getStartBlock()).thenReturn(BigInteger.TEN);
        when(interactor.replayPastAndFutureBlocks(any()))
                .thenReturn(
                        Flowable.just(
                                new Block(
                                        BigInteger.ONE,
                                        "0x123",
                                        "1000",
                                        BigInteger.ZERO,
                                        BigInteger.TEN,
                                        BigInteger.TEN,
                                        List.of())));
        assertDoesNotThrow(
                () -> {
                    BlockSubscriber subscriber =
                            createBlockSubscriber(
                                    interactor,
                                    dispatcher,
                                    newNode(10, 1, 1),
                                    new BlockToBlockEventMapper(),
                                    calculator,
                                    circuitBreaker,
                                    retry);

                    Disposable disposable = subscriber.subscribe();
                    assertNotNull(disposable);
                    assertTrue(disposable.isDisposed());
                });
    }
}
