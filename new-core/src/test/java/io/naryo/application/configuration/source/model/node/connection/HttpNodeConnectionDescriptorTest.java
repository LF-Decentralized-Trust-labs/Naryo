package io.naryo.application.configuration.source.model.node.connection;

import java.time.Duration;
import java.util.Optional;

import io.naryo.application.configuration.source.model.DescriptorTestDurationArgumentsProvider;
import io.naryo.application.configuration.source.model.DescriptorTestIntegerArgumentsProvider;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class HttpNodeConnectionDescriptorTest extends NodeConnectionDescriptorTest {

    HttpNodeConnectionDescriptor getNodeConnectionDescriptor() {
        return new DummyHttpNodeConnectionDescriptor();
    }

    @Test
    void testMerge_differentDescriptor() {
        HttpNodeConnectionDescriptor original = getNodeConnectionDescriptor();
        NodeConnectionDescriptor other =
                new WsNodeConnectionDescriptorTest.DummyWsNodeConnectionDescriptor();

        HttpNodeConnectionDescriptor result = (HttpNodeConnectionDescriptor) original.merge(other);

        assertEquals(
                original,
                result,
                "Should return the original WsNodeConnectionDescriptor when merging with a different descriptor");
    }

    @ParameterizedTest
    @ArgumentsSource(DescriptorTestIntegerArgumentsProvider.class)
    void testMerge_maxIdleConnections(
            Integer originalMaxIdleConnections,
            Integer otherMaxIdleConnections,
            Integer expectedMaxIdleConnections) {
        HttpNodeConnectionDescriptor original = this.getNodeConnectionDescriptor();
        original.setMaxIdleConnections(originalMaxIdleConnections);
        HttpNodeConnectionDescriptor other = this.getNodeConnectionDescriptor();
        other.setMaxIdleConnections(otherMaxIdleConnections);

        HttpNodeConnectionDescriptor result = (HttpNodeConnectionDescriptor) original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedMaxIdleConnections),
                result.getMaxIdleConnections(),
                "Should merge the max idle connections");
    }

    @ParameterizedTest
    @ArgumentsSource(DescriptorTestDurationArgumentsProvider.class)
    void testMerge_keepAliveDuration(
            Duration originalKeepAliveDuration,
            Duration otherKeepAliveDuration,
            Duration expectedKeepAliveDuration) {
        HttpNodeConnectionDescriptor original = this.getNodeConnectionDescriptor();
        original.setKeepAliveDuration(originalKeepAliveDuration);
        HttpNodeConnectionDescriptor other = this.getNodeConnectionDescriptor();
        other.setKeepAliveDuration(otherKeepAliveDuration);

        HttpNodeConnectionDescriptor result = (HttpNodeConnectionDescriptor) original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedKeepAliveDuration),
                result.getKeepAliveDuration(),
                "Should merge the keep alive duration");
    }

    @ParameterizedTest
    @ArgumentsSource(DescriptorTestDurationArgumentsProvider.class)
    void testMerge_connectionTimeout(
            Duration originalConnectionTimeout,
            Duration otherConnectionTimeout,
            Duration expectedConnectionTimeout) {
        HttpNodeConnectionDescriptor original = this.getNodeConnectionDescriptor();
        original.setConnectionTimeout(originalConnectionTimeout);
        HttpNodeConnectionDescriptor other = this.getNodeConnectionDescriptor();
        other.setConnectionTimeout(otherConnectionTimeout);

        HttpNodeConnectionDescriptor result = (HttpNodeConnectionDescriptor) original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedConnectionTimeout),
                result.getConnectionTimeout(),
                "Should merge the connection timeout");
    }

    @ParameterizedTest
    @ArgumentsSource(DescriptorTestDurationArgumentsProvider.class)
    void testMerge_readTimeout(
            Duration originalReadTimeout, Duration otherReadTimeout, Duration expectedReadTimeout) {
        HttpNodeConnectionDescriptor original = this.getNodeConnectionDescriptor();
        original.setReadTimeout(originalReadTimeout);
        HttpNodeConnectionDescriptor other = this.getNodeConnectionDescriptor();
        other.setReadTimeout(otherReadTimeout);

        HttpNodeConnectionDescriptor result = (HttpNodeConnectionDescriptor) original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedReadTimeout),
                result.getReadTimeout(),
                "Should merge the read timeout");
    }

    @Setter
    public static class DummyHttpNodeConnectionDescriptor extends DummyNodeConnectionDescriptor
            implements HttpNodeConnectionDescriptor {
        private Integer maxIdleConnections;
        private Duration keepAliveDuration;
        private Duration connectionTimeout;
        private Duration readTimeout;

        @Override
        public Optional<Integer> getMaxIdleConnections() {
            return Optional.ofNullable(maxIdleConnections);
        }

        @Override
        public Optional<Duration> getKeepAliveDuration() {
            return Optional.ofNullable(keepAliveDuration);
        }

        @Override
        public Optional<Duration> getConnectionTimeout() {
            return Optional.ofNullable(connectionTimeout);
        }

        @Override
        public Optional<Duration> getReadTimeout() {
            return Optional.ofNullable(readTimeout);
        }
    }
}
