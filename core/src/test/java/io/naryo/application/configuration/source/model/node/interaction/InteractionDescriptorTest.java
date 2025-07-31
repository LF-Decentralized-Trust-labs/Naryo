package io.naryo.application.configuration.source.model.node.interaction;

import lombok.Setter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
abstract class InteractionDescriptorTest {

    abstract InteractionDescriptor getInteractionDescriptor();

    @Setter
    protected abstract static class DummyInteractionDescriptor implements InteractionDescriptor {}
}
