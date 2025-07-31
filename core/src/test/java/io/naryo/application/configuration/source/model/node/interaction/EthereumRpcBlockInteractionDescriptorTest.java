package io.naryo.application.configuration.source.model.node.interaction;

import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class EthereumRpcBlockInteractionDescriptorTest extends BlockInteractionDescriptorTest {

    @Override
    protected EthereumRpcBlockInteractionDescriptor getInteractionDescriptor() {
        return new DummyEthereumRpcBlockInteractionDescriptor();
    }

    @Test
    void testMerge_differentDescriptor() {
        EthereumRpcBlockInteractionDescriptor original = getInteractionDescriptor();
        InteractionDescriptor other =
                new HederaMirrorNodeBlockInteractionDescriptorTest
                        .DummyHederaRpcBlockInteractionDescriptor();

        EthereumRpcBlockInteractionDescriptor result =
                (EthereumRpcBlockInteractionDescriptor) original.merge(other);

        assertEquals(
                original,
                result,
                "Should return the original EthereumRpcBlockInteractionDescriptor when merging with a different descriptor");
    }

    @Setter
    protected static class DummyEthereumRpcBlockInteractionDescriptor
            extends DummyBlockInteractionDescriptor
            implements EthereumRpcBlockInteractionDescriptor {}
}
