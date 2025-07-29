package io.naryo.application.configuration.source.model.node.interaction;

import java.util.Optional;

import io.naryo.application.configuration.source.model.DescriptorTestIntegerArgumentsProvider;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class HederaMirrorNodeBlockInteractionDescriptorTest extends BlockInteractionDescriptorTest {

    @Override
    protected DummyHederaRpcBlockInteractionDescriptor getInteractionDescriptor() {
        return new DummyHederaRpcBlockInteractionDescriptor();
    }

    @Test
    void testMerge_differentDescriptor() {
        HederaMirrorNodeBlockInteractionDescriptor original = getInteractionDescriptor();
        InteractionDescriptor other =
                new EthereumRpcBlockInteractionDescriptorTest
                        .DummyEthereumRpcBlockInteractionDescriptor();

        HederaMirrorNodeBlockInteractionDescriptor result =
                (HederaMirrorNodeBlockInteractionDescriptor) original.merge(other);

        assertEquals(
                original,
                result,
                "Should return the original HederaMirrorNodeBlockInteractionDescriptor when merging with a different descriptor");
    }

    @ParameterizedTest
    @ArgumentsSource(DescriptorTestIntegerArgumentsProvider.class)
    void testMerge_limitPerRequest(
            Integer originalLimitPerRequest,
            Integer otherLimitPerRequest,
            Integer expectedLimitPerRequest) {
        HederaMirrorNodeBlockInteractionDescriptor original = getInteractionDescriptor();
        original.setLimitPerRequest(originalLimitPerRequest);
        HederaMirrorNodeBlockInteractionDescriptor other = getInteractionDescriptor();
        other.setLimitPerRequest(otherLimitPerRequest);

        HederaMirrorNodeBlockInteractionDescriptor result =
                (HederaMirrorNodeBlockInteractionDescriptor) original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedLimitPerRequest),
                result.getLimitPerRequest(),
                "Should merge the limit per request");
    }

    @ParameterizedTest
    @ArgumentsSource(DescriptorTestIntegerArgumentsProvider.class)
    void testMerge_retriesPerRequest(
            Integer originalRetriesPerRequest,
            Integer otherRetriesPerRequest,
            Integer expectedRetriesPerRequest) {
        HederaMirrorNodeBlockInteractionDescriptor original = getInteractionDescriptor();
        original.setRetriesPerRequest(originalRetriesPerRequest);
        HederaMirrorNodeBlockInteractionDescriptor other = getInteractionDescriptor();
        other.setRetriesPerRequest(otherRetriesPerRequest);

        HederaMirrorNodeBlockInteractionDescriptor result =
                (HederaMirrorNodeBlockInteractionDescriptor) original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedRetriesPerRequest),
                result.getRetriesPerRequest(),
                "Should merge the retries per request");
    }

    @Setter
    public static class DummyHederaRpcBlockInteractionDescriptor
            extends DummyBlockInteractionDescriptor
            implements HederaMirrorNodeBlockInteractionDescriptor {
        private Integer limitPerRequest;
        private Integer retriesPerRequest;

        @Override
        public Optional<Integer> getLimitPerRequest() {
            return Optional.ofNullable(limitPerRequest);
        }

        @Override
        public Optional<Integer> getRetriesPerRequest() {
            return Optional.ofNullable(retriesPerRequest);
        }
    }
}
