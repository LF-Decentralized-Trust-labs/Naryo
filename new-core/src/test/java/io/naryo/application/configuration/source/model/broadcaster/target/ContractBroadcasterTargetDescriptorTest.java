package io.naryo.application.configuration.source.model.broadcaster.target;

import lombok.Setter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ContractBroadcasterTargetDescriptorTest extends BroadcasterTargetDescriptorTest {

    protected BroadcasterTargetDescriptor getBroadcasterDescriptor() {
        return new DummyContractBroadcasterTargetDescriptor();
    }

    @Setter
    public static class DummyContractBroadcasterTargetDescriptor
            extends DummyBroadcasterTargetDescriptor
            implements ContractEventBroadcasterTargetDescriptor {}
}
