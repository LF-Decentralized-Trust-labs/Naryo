package io.naryo.application.configuration.revision.hook;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.diff.DiffResult;
import io.naryo.application.configuration.revision.registry.LiveRegistry;
import io.naryo.application.node.NodeInitializer;
import io.naryo.application.node.NodeLifecycle;
import io.naryo.application.node.NodeRunner;
import io.naryo.application.node.revision.NodeConfigurationRevisionManager;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.eth.pub.PublicEthereumNodeBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultStoreConfigurationRuntimeHookTest {

    private @Mock NodeLifecycle nodeLifecycle;
    private @Mock NodeInitializer nodeInitializer;
    private @Mock NodeConfigurationRevisionManager nodeConfigurationRevisionManager;

    private DefaultStoreConfigurationRuntimeHook hook;

    @BeforeEach
    void setUp() {
        hook =
                new DefaultStoreConfigurationRuntimeHook(
                        nodeLifecycle, nodeInitializer, nodeConfigurationRevisionManager);
    }

    @Test
    void onAfterApply_shouldStopNodes_whenRemovedNotEmpty() {
        StoreConfiguration storeConfigurationA = mock();
        StoreConfiguration storeConfigurationB = mock();
        UUID nodeIdA = UUID.randomUUID();
        UUID nodeIdB = UUID.randomUUID();

        doReturn(nodeIdA).when(storeConfigurationA).getNodeId();
        doReturn(nodeIdB).when(storeConfigurationB).getNodeId();

        DiffResult<StoreConfiguration> diff = mock();
        doReturn(List.of(storeConfigurationA, storeConfigurationB)).when(diff).removed();
        doReturn(List.of()).when(diff).modified();

        assertDoesNotThrow(() -> hook.onAfterApply(mock(Revision.class), diff));

        verify(nodeLifecycle, times(1)).stop(nodeIdA);
        verify(nodeLifecycle, times(1)).stop(nodeIdB);
        verifyNoMoreInteractions(nodeLifecycle);
        verifyNoInteractions(nodeInitializer);
    }

    @Test
    void onAfterApply_shouldRestartNodes_whenModifiedNotEmpty() {
        StoreConfiguration after = mock();
        UUID nodeId = UUID.randomUUID();
        Node node = new PublicEthereumNodeBuilder().withId(nodeId).build();

        doReturn(nodeId).when(after).getNodeId();

        DiffResult.Modified<StoreConfiguration> modified = mock();
        doReturn(after).when(modified).after();

        LiveRegistry<Node> nodeLiveRegistry = mock(LiveRegistry.class);
        doReturn(nodeLiveRegistry).when(nodeConfigurationRevisionManager).liveRegistry();
        Revision<Node> revision = new Revision<>(1, "test-hash", List.of(node));
        doReturn(revision).when(nodeLiveRegistry).active();

        NodeRunner newRunner = mock();
        doReturn(newRunner).when(nodeInitializer).initializeNode(node);

        DiffResult<StoreConfiguration> diff = mock();
        doReturn(List.of()).when(diff).removed();
        doReturn(List.of(modified)).when(diff).modified();

        assertDoesNotThrow(() -> hook.onAfterApply(mock(Revision.class), diff));

        verify(nodeInitializer, times(1)).initializeNode(node);
        ArgumentCaptor<Supplier<NodeRunner>> argument = ArgumentCaptor.forClass(Supplier.class);
        verify(nodeLifecycle, times(1)).restart(eq(nodeId), argument.capture());
        assertEquals(argument.getValue().get(), newRunner);
        verifyNoMoreInteractions(nodeLifecycle);
    }

    @Test
    void onAfterApply_shouldHandleAllSections_whenAllPresent() {
        StoreConfiguration removed = mock();
        StoreConfiguration after = mock();

        UUID nodeIdToRestart = UUID.randomUUID();
        doReturn(nodeIdToRestart).when(after).getNodeId();
        Node nodeToRestart = new PublicEthereumNodeBuilder().withId(nodeIdToRestart).build();

        UUID nodeIdToRemove = UUID.randomUUID();
        doReturn(nodeIdToRemove).when(removed).getNodeId();

        NodeRunner restartedRunner = mock();
        doReturn(restartedRunner).when(nodeInitializer).initializeNode(nodeToRestart);

        DiffResult.Modified<Node> modified = mock();
        doReturn(after).when(modified).after();

        LiveRegistry<Node> nodeLiveRegistry = mock(LiveRegistry.class);
        doReturn(nodeLiveRegistry).when(nodeConfigurationRevisionManager).liveRegistry();
        Revision<Node> revision = new Revision<>(1, "test-hash", List.of(nodeToRestart));
        doReturn(revision).when(nodeLiveRegistry).active();

        DiffResult<StoreConfiguration> diff = mock();
        doReturn(List.of(removed)).when(diff).removed();
        doReturn(List.of(modified)).when(diff).modified();

        assertDoesNotThrow(() -> hook.onAfterApply(mock(Revision.class), diff));

        verify(nodeLifecycle, times(1)).stop(nodeIdToRemove);
        verify(nodeInitializer, times(1)).initializeNode(nodeToRestart);
        ArgumentCaptor<Supplier<NodeRunner>> argument = ArgumentCaptor.forClass(Supplier.class);
        verify(nodeLifecycle, times(1)).restart(eq(nodeIdToRestart), argument.capture());
        assertEquals(argument.getValue().get(), restartedRunner);
        verifyNoMoreInteractions(nodeLifecycle);
    }
}
