package io.naryo.application.configuration.source.model.node;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import io.naryo.application.configuration.source.model.DescriptorTestStringArgumentsProvider;
import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.HederaMirrorNodeBlockInteractionDescriptorTest;
import io.naryo.application.configuration.source.model.node.interaction.InteractionDescriptor;
import io.naryo.application.configuration.source.model.node.subscription.PubsubBlockSubscriptionDescriptorTest;
import io.naryo.application.configuration.source.model.node.subscription.SubscriptionDescriptor;
import io.naryo.domain.node.NodeType;
import lombok.Getter;
import lombok.Setter;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
abstract class NodeDescriptorTest {

    protected abstract NodeDescriptor getNodeDescriptor();

    @Test
    void testMerge_differentFilterType() {
        NodeDescriptor original =
                new DummyNodeDescriptor() {
                    @Override
                    public NodeType getType() {
                        return NodeType.ETHEREUM;
                    }
                };
        NodeDescriptor other =
                new DummyNodeDescriptor() {
                    @Override
                    public NodeType getType() {
                        return NodeType.HEDERA;
                    }
                };

        NodeDescriptor result = original.merge(other);

        assertEquals(
                original,
                result,
                "Should return the original NodeDescriptor when merging with a different type");
    }

    @ParameterizedTest
    @ArgumentsSource(DescriptorTestStringArgumentsProvider.class)
    void testMerge_name(String originalName, String otherName, String expectedName) {
        NodeDescriptor original = this.getNodeDescriptor();
        original.setName(originalName);
        NodeDescriptor other = this.getNodeDescriptor();
        other.setName(otherName);

        NodeDescriptor result = original.merge(other);

        assertEquals(Optional.ofNullable(expectedName), result.getName(), "Should merge the name");
    }

    @ParameterizedTest
    @MethodSource("subscriptionParameters")
    void testMerge_subscription(
            SubscriptionDescriptor originalSubscription,
            SubscriptionDescriptor otherSubscription,
            SubscriptionDescriptor expectedSubscription) {
        NodeDescriptor original = getNodeDescriptor();
        original.setSubscription(originalSubscription);
        NodeDescriptor other = getNodeDescriptor();
        other.setSubscription(otherSubscription);

        NodeDescriptor result = original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedSubscription),
                result.getSubscription(),
                "Should merge the subscription");
    }

    private static Stream<Arguments> subscriptionParameters() {
        SubscriptionDescriptor original =
                Instancio.create(
                        PubsubBlockSubscriptionDescriptorTest.DummyPubsubBlockSubscriptionDescriptor
                                .class);
        SubscriptionDescriptor other =
                Instancio.create(
                        PubsubBlockSubscriptionDescriptorTest.DummyPubsubBlockSubscriptionDescriptor
                                .class);

        return Stream.of(
                Arguments.of(original, other, original),
                Arguments.of(null, other, other),
                Arguments.of(null, null, null));
    }

    @ParameterizedTest
    @MethodSource("interactionParameters")
    void testMerge_interaction(
            InteractionDescriptor originalInteraction,
            InteractionDescriptor otherInteraction,
            InteractionDescriptor expectedInteraction) {
        NodeDescriptor original = getNodeDescriptor();
        original.setInteraction(originalInteraction);
        NodeDescriptor other = getNodeDescriptor();
        other.setInteraction(otherInteraction);

        NodeDescriptor result = original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedInteraction),
                result.getInteraction(),
                "Should merge the interaction");
    }

    private static Stream<Arguments> interactionParameters() {
        InteractionDescriptor original =
                Instancio.create(
                        HederaMirrorNodeBlockInteractionDescriptorTest
                                .DummyHederaRpcBlockInteractionDescriptor.class);
        InteractionDescriptor other =
                Instancio.create(
                        HederaMirrorNodeBlockInteractionDescriptorTest
                                .DummyHederaRpcBlockInteractionDescriptor.class);

        return Stream.of(
                Arguments.of(original, other, original),
                Arguments.of(null, other, other),
                Arguments.of(null, null, null));
    }

    //
    //    @ParameterizedTest
    //    @MethodSource("nodeConnectionParameters")
    //    void testMerge_nodeConnection(
    //        NodeConnectionDescriptor originalConnection,
    //        NodeConnectionDescriptor otherConnection,
    //        NodeConnectionDescriptor expectedConnection) {
    //        NodeDescriptor original = getNodeDescriptor();
    //        original.setConnection(originalConnection);
    //        NodeDescriptor other = getNodeDescriptor();
    //        other.setConnection(otherConnection);
    //
    //        NodeDescriptor result = original.merge(other);
    //
    //        assertEquals(Optional.ofNullable(expectedConnection), result.getConnection(), "Should
    // merge the connection");
    //    }
    //
    //    private static Stream<Arguments> nodeConnectionParameters() {
    //        NodeDescriptor original =
    //            Instancio.create(
    //                BlockFilterSyncDescriptorTest.DummyBlockFilterSyncDescriptor.class);
    //        NodeDescriptor other =
    //            Instancio.create(
    //                BlockFilterSyncDescriptorTest.DummyBlockFilterSyncDescriptor.class);
    //
    //        return Stream.of(
    //            Arguments.of(original, other, original),
    //            Arguments.of(null, other, other),
    //            Arguments.of(null, null, null));
    //    }

    @Setter
    protected abstract static class DummyNodeDescriptor implements NodeDescriptor {
        private final @Getter UUID id;
        private String name;
        private SubscriptionDescriptor subscription;
        private InteractionDescriptor interaction;
        private NodeConnectionDescriptor connection;

        public DummyNodeDescriptor() {
            this.id = UUID.randomUUID();
        }

        @Override
        public Optional<String> getName() {
            return Optional.ofNullable(name);
        }

        @Override
        public Optional<SubscriptionDescriptor> getSubscription() {
            return Optional.ofNullable(subscription);
        }

        @Override
        public Optional<InteractionDescriptor> getInteraction() {
            return Optional.ofNullable(interaction);
        }

        @Override
        public Optional<NodeConnectionDescriptor> getConnection() {
            return Optional.ofNullable(connection);
        }
    }
}
