package io.naryo.application.configuration.source.model.node.connection;

import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class WsNodeConnectionDescriptorTest extends NodeConnectionDescriptorTest {

    WsNodeConnectionDescriptor getNodeConnectionDescriptor() {
        return new DummyWsNodeConnectionDescriptor();
    }

    @Test
    void testMerge_differentDescriptor() {
        WsNodeConnectionDescriptor original = getNodeConnectionDescriptor();
        NodeConnectionDescriptor other =
                new HttpNodeConnectionDescriptorTest.DummyHttpNodeConnectionDescriptor();

        WsNodeConnectionDescriptor result = (WsNodeConnectionDescriptor) original.merge(other);

        assertEquals(
                original,
                result,
                "Should return the original WsNodeConnectionDescriptor when merging with a different descriptor");
    }

    @Setter
    protected static class DummyWsNodeConnectionDescriptor extends DummyNodeConnectionDescriptor
            implements WsNodeConnectionDescriptor {}
}
