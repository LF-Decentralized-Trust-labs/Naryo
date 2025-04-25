package io.librevents.application.node;

import java.time.Duration;
import java.util.UUID;

import io.librevents.application.filter.Synchronizer;
import io.librevents.application.node.subscription.Subscriber;
import io.librevents.domain.node.Node;
import io.librevents.domain.node.NodeName;
import io.librevents.domain.node.connection.NodeConnection;
import io.librevents.domain.node.connection.NodeConnectionType;
import io.librevents.domain.node.connection.RetryConfiguration;
import io.librevents.domain.node.connection.endpoint.ConnectionEndpoint;
import io.librevents.domain.node.hedera.HederaNode;
import io.librevents.domain.node.interaction.InteractionConfiguration;
import io.librevents.domain.node.interaction.InteractionStrategy;
import io.librevents.domain.node.subscription.SubscriptionConfiguration;
import io.librevents.domain.node.subscription.SubscriptionStrategy;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultNodeRunnerTest {

    private static class MockSubscriber implements Subscriber {
        @Override
        public Disposable subscribe() {
            return new CompositeDisposable();
        }
    }

    private static class MockSynchronizer implements Synchronizer {
        @Override
        public Disposable synchronize() {
            return new CompositeDisposable();
        }
    }

    private static class MockSubscriptionConfiguration extends SubscriptionConfiguration {
        public MockSubscriptionConfiguration() {
            super(SubscriptionStrategy.BLOCK_BASED);
        }
    }

    private static class MockInteractionConfiguration extends InteractionConfiguration {
        public MockInteractionConfiguration() {
            super(InteractionStrategy.BLOCK_BASED);
        }
    }

    private static class MockNodeConnection extends NodeConnection {
        protected MockNodeConnection() {
            super(
                    NodeConnectionType.HTTP,
                    new ConnectionEndpoint("https://test.com"),
                    new RetryConfiguration(1, Duration.ofMinutes(1)));
        }
    }

    @Test
    void testRun() {
        Node node =
                new HederaNode(
                        UUID.randomUUID(),
                        new NodeName("test"),
                        new MockSubscriptionConfiguration(),
                        new MockInteractionConfiguration(),
                        new MockNodeConnection());
        Subscriber subscriber = new MockSubscriber();
        Synchronizer synchronizer = new MockSynchronizer();

        DefaultNodeRunner runner = new DefaultNodeRunner(node, subscriber, synchronizer);

        CompositeDisposable disposable = runner.run();

        assertTrue(disposable.size() > 0, "Disposable should not be empty");
    }
}
