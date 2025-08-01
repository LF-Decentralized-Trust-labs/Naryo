package io.naryo.application.node;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

import io.naryo.application.filter.Synchronizer;
import io.naryo.application.node.subscription.Subscriber;
import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.NodeName;
import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.connection.NodeConnectionType;
import io.naryo.domain.node.connection.RetryConfiguration;
import io.naryo.domain.node.hedera.HederaNode;
import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.interaction.InteractionStrategy;
import io.naryo.domain.node.subscription.SubscriptionConfiguration;
import io.naryo.domain.node.subscription.SubscriptionStrategy;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultNodeRunnerTest {

    @Test
    void testRun() throws IOException {
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
}
