package io.librevents.application.node.subscription.block.poll;

import java.math.BigInteger;

import io.librevents.application.node.dispatch.Dispatcher;
import io.librevents.application.node.interactor.block.BlockInteractor;
import io.librevents.application.node.interactor.block.mapper.BlockToBlockEventMapper;
import io.librevents.application.node.subscription.block.BlockSubscriber;
import io.librevents.application.node.subscription.block.BlockSubscriberTest;
import io.librevents.domain.node.Node;
import io.reactivex.disposables.Disposable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        assertDoesNotThrow(() -> {
            BlockSubscriber subscriber =
                createBlockSubscriber(
                    new MockBlockInteractor(),
                    new MockDispatcher(),
                    new MockNode(BigInteger.TEN, BigInteger.ZERO),
                    new BlockToBlockEventMapper());

            Disposable disposable = subscriber.subscribe();
            assertNotNull(disposable);
            assertTrue(disposable.isDisposed());
        });
    }
}
