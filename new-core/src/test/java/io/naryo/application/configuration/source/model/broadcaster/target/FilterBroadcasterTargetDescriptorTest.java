package io.naryo.application.configuration.source.model.broadcaster.target;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FilterBroadcasterTargetDescriptorTest extends BroadcasterTargetDescriptorTest {

    protected BroadcasterTargetDescriptor getBroadcasterDescriptor() {
        return new DummyFilterBroadcasterTargetDescriptor(UUID.randomUUID());
    }

    @Setter
    @Getter
    public static class DummyFilterBroadcasterTargetDescriptor
            extends DummyBroadcasterTargetDescriptor implements FilterBroadcasterTargetDescriptor {
        UUID filterId;

        public DummyFilterBroadcasterTargetDescriptor(UUID filterId) {
            super();
            this.filterId = filterId;
        }
    }
}
