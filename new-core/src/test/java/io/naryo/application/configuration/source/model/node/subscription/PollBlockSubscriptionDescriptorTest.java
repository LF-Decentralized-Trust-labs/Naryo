package io.naryo.application.configuration.source.model.node.subscription;

import java.time.Duration;
import java.util.Optional;

import io.naryo.application.configuration.source.model.DescriptorTestDurationArgumentsProvider;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PollBlockSubscriptionDescriptorTest extends BlockSubscriptionDescriptorTest {

    protected PollBlockSubscriptionDescriptor getSubscriptionDescriptor() {
        return new DummyPollBlockSubscriptionDescriptor();
    }

    @Test
    void testMerge_differentDescriptor() {
        BlockSubscriptionDescriptor original = getSubscriptionDescriptor();
        SubscriptionDescriptor other =
                new PubsubBlockSubscriptionDescriptorTest.DummyPubsubBlockSubscriptionDescriptor();

        BlockSubscriptionDescriptor result = (BlockSubscriptionDescriptor) original.merge(other);

        assertEquals(
                original,
                result,
                "Should return the original PollBlockSubscriptionDescriptor when merging with a different descriptor");
    }

    @ParameterizedTest
    @ArgumentsSource(DescriptorTestDurationArgumentsProvider.class)
    void testMerge_interval(
            Duration originalInterval, Duration otherInterval, Duration expectedInterval) {
        PollBlockSubscriptionDescriptor original = getSubscriptionDescriptor();
        original.setInterval(originalInterval);
        PollBlockSubscriptionDescriptor other = getSubscriptionDescriptor();
        other.setInterval(otherInterval);

        PollBlockSubscriptionDescriptor result =
                (PollBlockSubscriptionDescriptor) original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedInterval),
                result.getInterval(),
                "Should merge the interval");
    }

    @Setter
    protected static class DummyPollBlockSubscriptionDescriptor
            extends DummyBlockSubscriptionDescriptor implements PollBlockSubscriptionDescriptor {
        private Duration interval;

        @Override
        public Optional<Duration> getInterval() {
            return Optional.ofNullable(interval);
        }
    }
}
