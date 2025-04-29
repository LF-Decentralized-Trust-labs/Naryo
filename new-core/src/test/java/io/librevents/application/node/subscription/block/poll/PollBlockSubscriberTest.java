package io.librevents.application.node.subscription.block.poll;

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

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PollBlockSubscriberTest extends BlockSubscriberTest {

    @Override
    protected BlockSubscriber createBlockSubscriber(
            BlockInteractor interactor,
            Dispatcher dispatcher,
            Node node,
            BlockToBlockEventMapper blockMapper) {
        return new PollBlockSubscriber(interactor, dispatcher, node, blockMapper);
    }

    @Test
    void testSubscribe() {
        when(interactor.replayPastAndFutureBlocks(any()))
                .thenReturn(
                        Flowable.just(
                                new Block(
                                        BigInteger.ONE,
                                        "0x123",
                                        BigInteger.ONE,
                                        "1000",
                                        BigInteger.ZERO,
                                        BigInteger.TEN,
                                        BigInteger.TEN,
                                        BigInteger.ZERO,
                                        List.of())));
        assertDoesNotThrow(
                () -> {
                    BlockSubscriber subscriber =
                            createBlockSubscriber(
                                    interactor,
                                    dispatcher,
                                    newNode(10, 0, 1, 1),
                                    new BlockToBlockEventMapper());

                    Disposable disposable = subscriber.subscribe();
                    assertNotNull(disposable);
                    assertTrue(disposable.isDisposed());
                });
    }
}
