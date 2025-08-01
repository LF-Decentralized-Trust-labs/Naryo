package io.naryo.application.configuration.source.model.broadcaster.target;

import java.util.Optional;

import io.naryo.application.configuration.source.model.DescriptorTestStringArgumentsProvider;
import io.naryo.domain.broadcaster.BroadcasterTargetType;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public abstract class BroadcasterTargetDescriptorTest {

    protected abstract BroadcasterTargetDescriptor getBroadcasterDescriptor();

    @Test
    void testMerge_differentTargetType() {
        BroadcasterTargetDescriptor original =
                new DummyBroadcasterTargetDescriptor() {
                    @Override
                    public BroadcasterTargetType getType() {
                        return BroadcasterTargetType.ALL;
                    }
                };
        BroadcasterTargetDescriptor other =
                new DummyBroadcasterTargetDescriptor() {
                    @Override
                    public BroadcasterTargetType getType() {
                        return BroadcasterTargetType.FILTER;
                    }
                };

        BroadcasterTargetDescriptor result = original.merge(other);

        assertEquals(
                original,
                result,
                "Should return the original BroadcasterTargetDescriptor when merging with a different BroadcasterTargetType");
    }

    @ParameterizedTest
    @ArgumentsSource(DescriptorTestStringArgumentsProvider.class)
    void testMerge_destination(
            String originalDestination, String otherDestination, String expectedDestination) {
        BroadcasterTargetDescriptor original = this.getBroadcasterDescriptor();
        original.setDestination(originalDestination);
        BroadcasterTargetDescriptor other = this.getBroadcasterDescriptor();
        other.setDestination(otherDestination);

        BroadcasterTargetDescriptor result = original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedDestination),
                result.getDestination(),
                "Should merge the destination");
    }

    @Setter
    public abstract static class DummyBroadcasterTargetDescriptor
            implements BroadcasterTargetDescriptor {
        private String destination;

        @Override
        public Optional<String> getDestination() {
            return Optional.ofNullable(destination);
        }
    }
}
