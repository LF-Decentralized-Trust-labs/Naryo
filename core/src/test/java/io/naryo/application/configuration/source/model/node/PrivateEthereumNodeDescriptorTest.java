package io.naryo.application.configuration.source.model.node;

import java.util.Optional;

import io.naryo.application.configuration.source.model.DescriptorTestStringArgumentsProvider;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PrivateEthereumNodeDescriptorTest extends EthereumNodeDescriptorTest {

    protected DummyPrivateEthereumNodeDescriptor getNodeDescriptor() {
        return new DummyPrivateEthereumNodeDescriptor();
    }

    @Test
    void testMerge_differentNodeDescriptor() {
        NodeDescriptor original = new DummyPrivateEthereumNodeDescriptor();
        NodeDescriptor other =
                new PublicEthereumNodeDescriptorTest.DummyPublicEthereumNodeDescriptor();

        NodeDescriptor result = original.merge(other);

        assertEquals(
                original,
                result,
                "Should return the original NodeDescriptor when merging with a different type");
    }

    @ParameterizedTest
    @ArgumentsSource(DescriptorTestStringArgumentsProvider.class)
    void testMerge_groupId(String originalGroupId, String otherGroupId, String expectedGroupId) {
        PrivateEthereumNodeDescriptor original = getNodeDescriptor();
        original.setGroupId(originalGroupId);
        PrivateEthereumNodeDescriptor other = getNodeDescriptor();
        other.setGroupId(otherGroupId);

        DummyPrivateEthereumNodeDescriptor result =
                (DummyPrivateEthereumNodeDescriptor) original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedGroupId),
                result.getGroupId(),
                "Should merge the group filterId");
    }

    @ParameterizedTest
    @ArgumentsSource(DescriptorTestStringArgumentsProvider.class)
    void testMerge_precompiledAddress(
            String originalPrecompiledAddress,
            String otherPrecompiledAddress,
            String expectedPrecompiledAddress) {
        PrivateEthereumNodeDescriptor original = getNodeDescriptor();
        original.setPrecompiledAddress(originalPrecompiledAddress);
        PrivateEthereumNodeDescriptor other = getNodeDescriptor();
        other.setPrecompiledAddress(otherPrecompiledAddress);

        DummyPrivateEthereumNodeDescriptor result =
                (DummyPrivateEthereumNodeDescriptor) original.merge(other);

        assertEquals(
                Optional.ofNullable(expectedPrecompiledAddress),
                result.getPrecompiledAddress(),
                "Should merge the precompiled ddress");
    }

    @Setter
    static class DummyPrivateEthereumNodeDescriptor extends DummyEthereumNodeDescriptor
            implements PrivateEthereumNodeDescriptor {
        private String groupId;
        private String precompiledAddress;

        @Override
        public Optional<String> getGroupId() {
            return Optional.ofNullable(groupId);
        }

        @Override
        public Optional<String> getPrecompiledAddress() {
            return Optional.ofNullable(precompiledAddress);
        }
    }
}
