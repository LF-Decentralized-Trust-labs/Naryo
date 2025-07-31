package io.naryo.application.configuration.source.model.filter.event.sync;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class FilterSyncDescriptorTest {

    public abstract static class DummyFilterSyncDescriptor implements FilterSyncDescriptor {}
}
