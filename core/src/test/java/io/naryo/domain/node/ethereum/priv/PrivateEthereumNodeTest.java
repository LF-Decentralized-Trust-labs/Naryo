package io.naryo.domain.node.ethereum.priv;

import java.util.UUID;

import io.naryo.domain.node.BaseNodeTest;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.NodeName;
import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.subscription.SubscriptionConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

final class PrivateEthereumNodeTest extends BaseNodeTest {

    private static final GroupId DEFAULT_GROUP_ID = new GroupId("test-group");
    private static final PrecompiledAddress DEFAULT_PRECOMPILED_ADDRESS =
            new PrecompiledAddress("test-address");

    @Override
    protected Node createNode(
            UUID id,
            NodeName name,
            SubscriptionConfiguration subscriptionConfiguration,
            InteractionConfiguration interactionConfiguration,
            NodeConnection connection) {
        return new PrivateEthereumNode(
                id,
                name,
                subscriptionConfiguration,
                interactionConfiguration,
                connection,
                DEFAULT_GROUP_ID,
                DEFAULT_PRECOMPILED_ADDRESS);
    }

    @Test
    void testGroupIdCannotBeNull() {
        assertThrows(
                NullPointerException.class,
                () ->
                        new PrivateEthereumNode(
                                UUID.randomUUID(),
                                new NodeName("test"),
                                new MockSubscriptionConfiguration(),
                                new MockInteractionConfiguration(),
                                new MockNodeConnection(),
                                null,
                                DEFAULT_PRECOMPILED_ADDRESS));
    }

    @Test
    void testPrecompiledAddressCannotBeNull() {
        assertThrows(
                NullPointerException.class,
                () ->
                        new PrivateEthereumNode(
                                UUID.randomUUID(),
                                new NodeName("test"),
                                new MockSubscriptionConfiguration(),
                                new MockInteractionConfiguration(),
                                new MockNodeConnection(),
                                DEFAULT_GROUP_ID,
                                null));
    }
}
