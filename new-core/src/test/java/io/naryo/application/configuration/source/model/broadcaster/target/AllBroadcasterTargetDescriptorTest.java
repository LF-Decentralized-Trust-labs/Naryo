package io.naryo.application.configuration.source.model.broadcaster.target;

import lombok.Setter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AllBroadcasterTargetDescriptorTest extends BroadcasterTargetDescriptorTest {

    protected BroadcasterTargetDescriptor getBroadcasterDescriptor() {
        return new DummyAllBroadcasterTargetDescriptor();
    }

    @Setter
    public static class DummyAllBroadcasterTargetDescriptor extends DummyBroadcasterTargetDescriptor
            implements AllBroadcasterTargetDescriptor {}
}
