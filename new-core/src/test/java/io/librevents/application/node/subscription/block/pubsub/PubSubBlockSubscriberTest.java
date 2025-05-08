package io.librevents.application.node.subscription.block.pubsub;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import io.librevents.application.node.calculator.StartBlockCalculator;
import io.librevents.application.node.dispatch.Dispatcher;
import io.librevents.application.node.interactor.block.BlockInteractor;
import io.librevents.application.node.interactor.block.dto.Block;
import io.librevents.application.node.interactor.block.mapper.BlockToBlockEventMapper;
import io.librevents.application.node.subscription.block.BlockSubscriber;
import io.librevents.application.node.subscription.block.BlockSubscriberTest;
import io.librevents.domain.node.Node;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PubSubBlockSubscriberTest extends BlockSubscriberTest {

    @Override
    protected BlockSubscriber createBlockSubscriber(
            BlockInteractor interactor,
            Dispatcher dispatcher,
            Node node,
            BlockToBlockEventMapper blockMapper,
            StartBlockCalculator calculator) {
        return new PubSubBlockSubscriber(interactor, dispatcher, node, blockMapper, calculator);
    }

    @Test
    void testSubscribe() throws IOException {
        when(interactor.replayPastBlocks(any()))
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
                                    calculator);

                    Disposable disposable = subscriber.subscribe();
                    assertNotNull(disposable);
                    assertFalse(disposable.isDisposed());
                });
    }
}
