package io.naryo.application.configuration.source.model.node;

import io.naryo.domain.node.NodeType;
import io.naryo.domain.node.ethereum.EthereumNodeVisibility;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
abstract class EthereumNodeDescriptorTest extends NodeDescriptorTest {

    protected abstract EthereumNodeDescriptor getNodeDescriptor();

    @Test
    void testMerge_differentNodeDescriptor() {
        NodeDescriptor original =
                new DummyEthereumNodeDescriptor() {
                    @Override
                    public EthereumNodeVisibility getVisibility() {
                        return EthereumNodeVisibility.PUBLIC;
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

    @Setter
    protected abstract static class DummyEthereumNodeDescriptor extends DummyNodeDescriptor
            implements EthereumNodeDescriptor {}
}
