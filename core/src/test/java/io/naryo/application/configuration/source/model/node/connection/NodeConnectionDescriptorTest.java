package io.naryo.application.configuration.source.model.node.connection;

import java.util.Optional;
import java.util.stream.Stream;

import io.naryo.application.configuration.source.model.node.connection.endpoint.ConnectionEndpointDescriptor;
import io.naryo.application.configuration.source.model.node.connection.endpoint.ConnectionEndpointDescriptorTest;
import io.naryo.application.configuration.source.model.node.connection.retry.NodeConnectionRetryDescriptor;
import io.naryo.application.configuration.source.model.node.connection.retry.NodeConnectionRetryDescriptorTest;
import io.naryo.domain.node.connection.NodeConnectionType;
import lombok.Setter;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
abstract class NodeConnectionDescriptorTest {

    abstract NodeConnectionDescriptor getNodeConnectionDescriptor();

    @Test
    void testMerge_differentType() {
        NodeConnectionDescriptor original =
                new DummyNodeConnectionDescriptor() {
                    @Override
                    public NodeConnectionType getType() {
                        return NodeConnectionType.WS;
                    }
                };
        NodeConnectionDescriptor other =
                new DummyNodeConnectionDescriptor() {
                    @Override
                    public NodeConnectionType getType() {
                        return NodeConnectionType.HTTP;
                    }
                };

        NodeConnectionDescriptor result = original.merge(other);

        assertEquals(
                original,
                result,
                "Should return the original NodeConnectionDescriptor when merging with a different type");
    }

    @ParameterizedTest
    @MethodSource("endpointParameters")
    void testMerge_endpoint(
            ConnectionEndpointDescriptor originalEndpoint,
            ConnectionEndpointDescriptor otherEndpoint,
            ConnectionEndpointDescriptor expectedEndpoint) {
        NodeConnectionDescriptor original = getNodeConnectionDescriptor();
        original.setEndpoint(originalEndpoint);
        NodeConnectionDescriptor other = getNodeConnectionDescriptor();
        other.setEndpoint(otherEndpoint);

        NodeConnectionDescriptor result = original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedEndpoint),
                result.getEndpoint(),
                "Should merge the endpoint");
    }

    private static Stream<Arguments> endpointParameters() {
        ConnectionEndpointDescriptor original =
                Instancio.create(
                        ConnectionEndpointDescriptorTest.DummyConnectionEndpointDescriptor.class);
        ConnectionEndpointDescriptor other =
                Instancio.create(
                        ConnectionEndpointDescriptorTest.DummyConnectionEndpointDescriptor.class);

        return Stream.of(
                Arguments.of(original, other, original),
                Arguments.of(null, other, other),
                Arguments.of(null, null, null));
    }

    @ParameterizedTest
    @MethodSource("retryParameters")
    void testMerge_retry(
            NodeConnectionRetryDescriptor originalRetry,
            NodeConnectionRetryDescriptor otherRetry,
            NodeConnectionRetryDescriptor expectedRetry) {
        NodeConnectionDescriptor original = getNodeConnectionDescriptor();
        original.setRetry(originalRetry);
        NodeConnectionDescriptor other = getNodeConnectionDescriptor();
        other.setRetry(otherRetry);

        NodeConnectionDescriptor result = original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedRetry), result.getRetry(), "Should merge the retry");
    }

    private static Stream<Arguments> retryParameters() {
        NodeConnectionRetryDescriptor original =
                Instancio.create(
                        NodeConnectionRetryDescriptorTest.DummyNodeConnectionRetryDescriptor.class);
        NodeConnectionRetryDescriptor other =
                Instancio.create(
                        NodeConnectionRetryDescriptorTest.DummyNodeConnectionRetryDescriptor.class);

        return Stream.of(
                Arguments.of(original, other, original),
                Arguments.of(null, other, other),
                Arguments.of(null, null, null));
    }

    @Setter
    protected abstract static class DummyNodeConnectionDescriptor
            implements NodeConnectionDescriptor {
        private ConnectionEndpointDescriptor endpoint;
        private NodeConnectionRetryDescriptor retry;

        @Override
        public Optional<ConnectionEndpointDescriptor> getEndpoint() {
            return Optional.ofNullable(endpoint);
        }

        @Override
        public Optional<NodeConnectionRetryDescriptor> getRetry() {
            return Optional.ofNullable(retry);
        }
    }
}
