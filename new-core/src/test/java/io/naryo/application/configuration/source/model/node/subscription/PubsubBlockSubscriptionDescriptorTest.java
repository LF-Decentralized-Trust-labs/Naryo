package io.naryo.application.configuration.source.model.node.subscription;

import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PubsubBlockSubscriptionDescriptorTest extends BlockSubscriptionDescriptorTest {

    protected DummyPubsubBlockSubscriptionDescriptor getSubscriptionDescriptor() {
        return new DummyPubsubBlockSubscriptionDescriptor();
    }

    @Test
    void testMerge_differentDescriptor() {
        BlockSubscriptionDescriptor original = getSubscriptionDescriptor();
        SubscriptionDescriptor other =
                new PollBlockSubscriptionDescriptorTest.DummyPollBlockSubscriptionDescriptor();

        BlockSubscriptionDescriptor result = (BlockSubscriptionDescriptor) original.merge(other);

        assertEquals(
                original,
                result,
                "Should return the original PubsubSubscriptionDescriptor when merging with a different descriptor");
    }

    @Setter
    public static class DummyPubsubBlockSubscriptionDescriptor
            extends DummyBlockSubscriptionDescriptor implements PubsubBlockSubscriptionDescriptor {}
}
