package io.naryo.application.configuration.source.model.filter.event.global;

import io.naryo.application.configuration.source.model.filter.event.EventFilterDescriptor;
import io.naryo.application.configuration.source.model.filter.event.EventFilterDescriptorTest;
import lombok.Setter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GlobalEventFilterDescriptorTest extends EventFilterDescriptorTest {

    @Override
    protected EventFilterDescriptor getFilterDescriptor() {
        return new DummyGlobalEventFilterDescriptor();
    }

    @Setter
    public static class DummyGlobalEventFilterDescriptor extends DummyEventFilterDescriptor
            implements GlobalEventFilterDescriptor {}
}
