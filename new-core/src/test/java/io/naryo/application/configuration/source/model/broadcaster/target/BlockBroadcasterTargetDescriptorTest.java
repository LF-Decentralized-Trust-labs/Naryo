package io.naryo.application.configuration.source.model.broadcaster.target;

import lombok.Setter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BlockBroadcasterTargetDescriptorTest extends BroadcasterTargetDescriptorTest {

    protected BroadcasterTargetDescriptor getBroadcasterDescriptor() {
        return new DummyBlockBroadcasterTargetDescriptor();
    }

    @Setter
    public static class DummyBlockBroadcasterTargetDescriptor
            extends DummyBroadcasterTargetDescriptor implements BlockBroadcasterTargetDescriptor {}
}
