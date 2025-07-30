package io.naryo.application.configuration.source.model.node.subscription;

import lombok.Setter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
abstract class SubscriptionDescriptorTest {

    abstract SubscriptionDescriptor getSubscriptionDescriptor();

    @Setter
    protected abstract static class DummySubscriptionDescriptor implements SubscriptionDescriptor {}
}
