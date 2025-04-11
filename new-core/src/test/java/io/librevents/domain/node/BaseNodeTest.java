package io.librevents.domain.node;

import java.time.Duration;
import java.util.UUID;

import io.librevents.domain.node.connection.NodeConnection;
import io.librevents.domain.node.connection.NodeConnectionType;
import io.librevents.domain.node.connection.RetryConfiguration;
import io.librevents.domain.node.connection.endpoint.ConnectionEndpoint;
import io.librevents.domain.node.interaction.InteractionConfiguration;
import io.librevents.domain.node.interaction.InteractionStrategy;
import io.librevents.domain.node.subscription.SubscriptionConfiguration;
import io.librevents.domain.node.subscription.SubscriptionStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class BaseNodeTest {

    protected static final String DEFAULT_NAME = "test";

    protected static final class MockSubscriptionConfiguration extends SubscriptionConfiguration {
        public MockSubscriptionConfiguration() {
            super(SubscriptionStrategy.BLOCK_BASED);
        }
    }

    protected static final class MockInteractionConfiguration extends InteractionConfiguration {
        public MockInteractionConfiguration() {
            super(InteractionStrategy.BLOCK_BASED);
        }
    }

    protected static final class MockNodeConnection extends NodeConnection {
        public MockNodeConnection() {
            super(
                    NodeConnectionType.HTTP,
                    new ConnectionEndpoint("http://localhost:8545"),
                    new RetryConfiguration(3, Duration.ofSeconds(1)));
        }
    }

    protected abstract Node createNode(
            UUID id,
            NodeName name,
            SubscriptionConfiguration subscriptionConfiguration,
            InteractionConfiguration interactionConfiguration,
            NodeConnection connection);

    @Test
    void testIdCannotBeNull() {
        assertThrows(
                NullPointerException.class,
                () ->
                        createNode(
                                null,
                                new NodeName(DEFAULT_NAME),
                                new MockSubscriptionConfiguration(),
                                new MockInteractionConfiguration(),
                                new MockNodeConnection()));
    }

    @Test
    void testNameCannotBeNull() {
        assertThrows(
                NullPointerException.class,
                () ->
                        createNode(
                                UUID.randomUUID(),
                                null,
                                new MockSubscriptionConfiguration(),
                                new MockInteractionConfiguration(),
                                new MockNodeConnection()));
    }

    @Test
    void testSubscriptionConfigurationCannotBeNull() {
        assertThrows(
                NullPointerException.class,
                () ->
                        createNode(
                                UUID.randomUUID(),
                                new NodeName(DEFAULT_NAME),
                                null,
                                new MockInteractionConfiguration(),
                                new MockNodeConnection()));
    }

    @Test
    void testInteractionConfigurationCannotBeNull() {
        assertThrows(
                NullPointerException.class,
                () ->
                        createNode(
                                UUID.randomUUID(),
                                new NodeName(DEFAULT_NAME),
                                new MockSubscriptionConfiguration(),
                                null,
                                new MockNodeConnection()));
    }

    @Test
    void testConnectionCannotBeNull() {
        assertThrows(
                NullPointerException.class,
                () ->
                        createNode(
                                UUID.randomUUID(),
                                new NodeName(DEFAULT_NAME),
                                new MockSubscriptionConfiguration(),
                                new MockInteractionConfiguration(),
                                null));
    }

    @Test
    void testReconfigure() {
        Node node =
                createNode(
                        UUID.randomUUID(),
                        new NodeName(DEFAULT_NAME),
                        new MockSubscriptionConfiguration(),
                        new MockInteractionConfiguration(),
                        new MockNodeConnection());
        NodeName newName = new NodeName("newName");
        SubscriptionConfiguration newSubscriptionConfiguration =
                new MockSubscriptionConfiguration();
        InteractionConfiguration newInteractionConfiguration = new MockInteractionConfiguration();
        NodeConnection newConnection = new MockNodeConnection();

        node.reconfigure(
                newName, newSubscriptionConfiguration, newInteractionConfiguration, newConnection);

        assertEquals(newName, node.getName());
        assertEquals(newSubscriptionConfiguration, node.getSubscriptionConfiguration());
        assertEquals(newInteractionConfiguration, node.getInteractionConfiguration());
        assertEquals(newConnection, node.getConnection());
    }

    @Test
    void testReconfigureWithNodeNameNull() {
        Node node =
                createNode(
                        UUID.randomUUID(),
                        new NodeName(DEFAULT_NAME),
                        new MockSubscriptionConfiguration(),
                        new MockInteractionConfiguration(),
                        new MockNodeConnection());

        assertThrows(
                NullPointerException.class,
                () ->
                        node.reconfigure(
                                null,
                                new MockSubscriptionConfiguration(),
                                new MockInteractionConfiguration(),
                                new MockNodeConnection()));
    }

    @Test
    void testReconfigureWithSubscriptionConfigurationNull() {
        Node node =
                createNode(
                        UUID.randomUUID(),
                        new NodeName(DEFAULT_NAME),
                        new MockSubscriptionConfiguration(),
                        new MockInteractionConfiguration(),
                        new MockNodeConnection());

        assertThrows(
                NullPointerException.class,
                () ->
                        node.reconfigure(
                                new NodeName(DEFAULT_NAME),
                                null,
                                new MockInteractionConfiguration(),
                                new MockNodeConnection()));
    }

    @Test
    void testReconfigureWithInteractionConfigurationNull() {
        Node node =
                createNode(
                        UUID.randomUUID(),
                        new NodeName(DEFAULT_NAME),
                        new MockSubscriptionConfiguration(),
                        new MockInteractionConfiguration(),
                        new MockNodeConnection());

        assertThrows(
                NullPointerException.class,
                () ->
                        node.reconfigure(
                                new NodeName(DEFAULT_NAME),
                                new MockSubscriptionConfiguration(),
                                null,
                                new MockNodeConnection()));
    }

    @Test
    void testReconfigureWithConnectionNull() {
        Node node =
                createNode(
                        UUID.randomUUID(),
                        new NodeName(DEFAULT_NAME),
                        new MockSubscriptionConfiguration(),
                        new MockInteractionConfiguration(),
                        new MockNodeConnection());

        assertThrows(
                NullPointerException.class,
                () ->
                        node.reconfigure(
                                new NodeName(DEFAULT_NAME),
                                new MockSubscriptionConfiguration(),
                                new MockInteractionConfiguration(),
                                null));
    }
}
