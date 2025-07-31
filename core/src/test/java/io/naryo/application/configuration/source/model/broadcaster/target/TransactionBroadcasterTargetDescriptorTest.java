package io.naryo.application.configuration.source.model.broadcaster.target;

import lombok.Setter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionBroadcasterTargetDescriptorTest extends BroadcasterTargetDescriptorTest {

    protected BroadcasterTargetDescriptor getBroadcasterDescriptor() {
        return new DummyTransactionBroadcasterTargetDescriptor();
    }

    @Setter
    public static class DummyTransactionBroadcasterTargetDescriptor
            extends DummyBroadcasterTargetDescriptor
            implements TransactionBroadcasterTargetDescriptor {}
}
