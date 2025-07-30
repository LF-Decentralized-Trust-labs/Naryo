package io.naryo.application.configuration.source.model.node;

import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PublicEthereumNodeDescriptorTest extends EthereumNodeDescriptorTest {

    protected EthereumNodeDescriptor getNodeDescriptor() {
        return new DummyPublicEthereumNodeDescriptor();
    }

    @Test
    void testMerge_differentNodeDescriptor() {
        NodeDescriptor original = new DummyPublicEthereumNodeDescriptor();
        NodeDescriptor other =
                new PrivateEthereumNodeDescriptorTest.DummyPrivateEthereumNodeDescriptor();

        NodeDescriptor result = original.merge(other);

        assertEquals(
                original,
                result,
                "Should return the original NodeDescriptor when merging with a different type");
    }

    @Setter
    static class DummyPublicEthereumNodeDescriptor extends DummyEthereumNodeDescriptor
            implements PublicEthereumNodeDescriptor {}
}
