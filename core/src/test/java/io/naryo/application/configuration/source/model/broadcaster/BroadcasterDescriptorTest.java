package io.naryo.application.configuration.source.model.broadcaster;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import io.naryo.application.configuration.source.model.DescriptorTestUuidArgumentsProvider;
import io.naryo.application.configuration.source.model.broadcaster.target.AllBroadcasterTargetDescriptorTest;
import io.naryo.application.configuration.source.model.broadcaster.target.BroadcasterTargetDescriptor;
import lombok.Getter;
import lombok.Setter;
import org.instancio.Instancio;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class BroadcasterDescriptorTest {

    @ParameterizedTest
    @ArgumentsSource(DescriptorTestUuidArgumentsProvider.class)
    void testMerge_configurationId(
            UUID originalConfigurationId, UUID otherConfigurationId, UUID expectedConfigurationId) {
        BroadcasterDescriptor original = new DummyBroadcasterDescriptor();
        original.setConfigurationId(originalConfigurationId);
        BroadcasterDescriptor other = new DummyBroadcasterDescriptor();
        other.setConfigurationId(otherConfigurationId);

        BroadcasterDescriptor result = original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedConfigurationId),
                result.getConfigurationId(),
                "Should merge the configurationId");
    }

    @ParameterizedTest
    @MethodSource("targetParameters")
    void testMerge_target(
            BroadcasterTargetDescriptor originalTarget,
            BroadcasterTargetDescriptor otherTarget,
            BroadcasterTargetDescriptor expectedTarget) {
        BroadcasterDescriptor original = new DummyBroadcasterDescriptor();
        original.setTarget(originalTarget);
        BroadcasterDescriptor other = new DummyBroadcasterDescriptor();
        other.setTarget(otherTarget);

        BroadcasterDescriptor result = original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedTarget), result.getTarget(), "Should merge the target");
    }

    private static Stream<Arguments> targetParameters() {
        BroadcasterTargetDescriptor original =
                Instancio.create(
                        AllBroadcasterTargetDescriptorTest.DummyAllBroadcasterTargetDescriptor
                                .class);
        BroadcasterTargetDescriptor other =
                Instancio.create(
                        AllBroadcasterTargetDescriptorTest.DummyAllBroadcasterTargetDescriptor
                                .class);

        return Stream.of(
                Arguments.of(original, other, original),
                Arguments.of(null, other, other),
                Arguments.of(null, null, null));
    }

    public static class DummyBroadcasterDescriptor implements BroadcasterDescriptor {
        private final @Getter UUID id;
        private @Setter UUID configurationId;
        private @Setter BroadcasterTargetDescriptor target;

        protected DummyBroadcasterDescriptor() {
            this.id = UUID.randomUUID();
        }

        @Override
        public Optional<UUID> getConfigurationId() {
            return Optional.ofNullable(configurationId);
        }

        @Override
        public Optional<BroadcasterTargetDescriptor> getTarget() {
            return Optional.ofNullable(target);
        }
    }
}
