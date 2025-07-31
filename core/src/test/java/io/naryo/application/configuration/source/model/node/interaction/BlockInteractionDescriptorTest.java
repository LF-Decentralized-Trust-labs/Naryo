package io.naryo.application.configuration.source.model.node.interaction;

import io.naryo.domain.node.interaction.block.InteractionMode;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
abstract class BlockInteractionDescriptorTest extends InteractionDescriptorTest {

    @Override
    protected abstract BlockInteractionDescriptor getInteractionDescriptor();

    @Test
    void testMerge_differentSubscriptionMethod() {
        BlockInteractionDescriptor original =
                new DummyBlockInteractionDescriptor() {
                    @Override
                    public InteractionMode getMode() {
                        return InteractionMode.HEDERA_MIRROR_NODE;
                    }
                };
        BlockInteractionDescriptor other =
                new DummyBlockInteractionDescriptor() {
                    @Override
                    public InteractionMode getMode() {
                        return InteractionMode.ETHEREUM_RPC;
                    }
                };

        BlockInteractionDescriptor result = (BlockInteractionDescriptor) original.merge(other);

        assertEquals(
                original,
                result,
                "Should return the original BlockInteractionDescriptor when merging with a different mode");
    }

    @Setter
    protected abstract static class DummyBlockInteractionDescriptor
            extends DummyInteractionDescriptor implements BlockInteractionDescriptor {}
}
