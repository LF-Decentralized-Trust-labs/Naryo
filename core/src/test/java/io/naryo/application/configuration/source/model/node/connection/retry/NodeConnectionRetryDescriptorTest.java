package io.naryo.application.configuration.source.model.node.connection.retry;

import java.time.Duration;
import java.util.Optional;

import io.naryo.application.configuration.source.model.DescriptorTestDurationArgumentsProvider;
import io.naryo.application.configuration.source.model.DescriptorTestIntegerArgumentsProvider;
import lombok.Setter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class NodeConnectionRetryDescriptorTest {

    @ParameterizedTest
    @ArgumentsSource(DescriptorTestIntegerArgumentsProvider.class)
    void testMerge_times(Integer originalTimes, Integer otherTimes, Integer expectedTimes) {
        NodeConnectionRetryDescriptor original = new DummyNodeConnectionRetryDescriptor();
        original.setTimes(originalTimes);
        NodeConnectionRetryDescriptor other = new DummyNodeConnectionRetryDescriptor();
        other.setTimes(otherTimes);

        NodeConnectionRetryDescriptor result = original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedTimes),
                result.getTimes(),
                "Should merge the retry times");
    }

    @ParameterizedTest
    @ArgumentsSource(DescriptorTestDurationArgumentsProvider.class)
    void testMerge_backoff(
            Duration originalBackoff, Duration otherBackoff, Duration expectedBackoff) {
        NodeConnectionRetryDescriptor original = new DummyNodeConnectionRetryDescriptor();
        original.setBackoff(originalBackoff);
        NodeConnectionRetryDescriptor other = new DummyNodeConnectionRetryDescriptor();
        other.setBackoff(otherBackoff);

        NodeConnectionRetryDescriptor result = original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedBackoff),
                result.getBackoff(),
                "Should merge the backoff duration");
    }

    @Setter
    public static class DummyNodeConnectionRetryDescriptor
            implements NodeConnectionRetryDescriptor {
        private Integer times;
        private Duration backoff;

        @Override
        public Optional<Integer> getTimes() {
            return Optional.ofNullable(times);
        }

        @Override
        public Optional<Duration> getBackoff() {
            return Optional.ofNullable(backoff);
        }
    }
}
