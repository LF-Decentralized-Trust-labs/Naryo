package io.naryo.application.configuration.revision.hook;

import java.util.List;
import java.util.UUID;

import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.diff.DiffResult;
import io.naryo.application.node.NodeInitializer;
import io.naryo.application.node.NodeLifecycle;
import io.naryo.application.node.NodeRunner;
import io.naryo.domain.node.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultNodeRuntimeHookTest {

    private @Mock NodeLifecycle nodeLifecycle;
    private @Mock NodeInitializer nodeInitializer;

    private DefaultNodeRuntimeHook hook;

    @BeforeEach
    void setUp() {
        hook = new DefaultNodeRuntimeHook(nodeLifecycle, nodeInitializer);
    }

    @Test
    void onAfterApply_shouldAddNodes_whenAddedNotEmpty() {
        Node nodeA = mock();
        Node nodeB = mock();
        NodeRunner runnerA = mock();
        NodeRunner runnerB = mock();

        doReturn(runnerA).when(nodeInitializer).initializeNode(nodeA);
        doReturn(runnerB).when(nodeInitializer).initializeNode(nodeB);

        DiffResult<Node> diff = mock();
        doReturn(List.of(nodeA, nodeB)).when(diff).added();
        doReturn(List.of()).when(diff).removed();
        doReturn(List.of()).when(diff).modified();

        assertDoesNotThrow(() -> hook.onAfterApply(mock(Revision.class), diff));

        verify(nodeInitializer, times(1)).initializeNode(nodeA);
        verify(nodeInitializer, times(1)).initializeNode(nodeB);
        verify(nodeLifecycle, times(1)).launch(runnerA);
        verify(nodeLifecycle, times(1)).launch(runnerB);
        verifyNoMoreInteractions(nodeLifecycle);
    }

    @Test
    void onAfterApply_shouldRemoveNodes_whenRemovedNotEmpty() {
        Node nodeA = mock();
        Node nodeB = mock();
        UUID nodeIdA = UUID.randomUUID();
        UUID nodeIdB = UUID.randomUUID();

        doReturn(nodeIdA).when(nodeA).getId();
        doReturn(nodeIdB).when(nodeB).getId();

        DiffResult<Node> diff = mock();
        doReturn(List.of()).when(diff).added();
        doReturn(List.of(nodeA, nodeB)).when(diff).removed();
        doReturn(List.of()).when(diff).modified();

        assertDoesNotThrow(() -> hook.onAfterApply(mock(Revision.class), diff));

        verify(nodeLifecycle, times(1)).stop(nodeIdA);
        verify(nodeLifecycle, times(1)).stop(nodeIdB);
        verifyNoMoreInteractions(nodeLifecycle);
        verifyNoInteractions(nodeInitializer);
    }

    @Test
    void onAfterApply_shouldUpdateNodes_whenModifiedNotEmpty() {
        Node before = mock();
        Node after = mock();
        UUID nodeId = UUID.randomUUID();

        doReturn(nodeId).when(before).getId();

        DiffResult.Modified<Node> modified = mock();
        doReturn(before).when(modified).before();
        doReturn(after).when(modified).after();

        NodeRunner newRunner = mock();
        doReturn(newRunner).when(nodeInitializer).initializeNode(after);

        DiffResult<Node> diff = mock();
        doReturn(List.of()).when(diff).added();
        doReturn(List.of()).when(diff).removed();
        doReturn(List.of(modified)).when(diff).modified();

        assertDoesNotThrow(() -> hook.onAfterApply(mock(Revision.class), diff));

        verify(nodeInitializer, times(1)).initializeNode(after);
        verify(nodeLifecycle, times(1)).restart(eq(nodeId), any());
        verifyNoMoreInteractions(nodeLifecycle);
    }

    @Test
    void onAfterApply_shouldHandleAllSections_whenAllPresent() {
        Node added = mock();
        Node removed = mock();
        Node before = mock();
        Node after = mock();

        UUID removeId = UUID.randomUUID();
        UUID beforeId = UUID.randomUUID();

        doReturn(removeId).when(removed).getId();
        doReturn(beforeId).when(before).getId();

        NodeRunner addedRunner = mock();
        NodeRunner updatedRunner = mock();

        doReturn(addedRunner).when(nodeInitializer).initializeNode(added);
        doReturn(updatedRunner).when(nodeInitializer).initializeNode(after);

        DiffResult.Modified<Node> modified = mock();
        doReturn(before).when(modified).before();
        doReturn(after).when(modified).after();

        DiffResult<Node> diff = mock();
        doReturn(List.of(added)).when(diff).added();
        doReturn(List.of(removed)).when(diff).removed();
        doReturn(List.of(modified)).when(diff).modified();

        assertDoesNotThrow(() -> hook.onAfterApply(mock(Revision.class), diff));

        verify(nodeInitializer, times(1)).initializeNode(added);
        verify(nodeInitializer, times(1)).initializeNode(after);
        verify(nodeLifecycle, times(1)).launch(addedRunner);
        verify(nodeLifecycle, times(1)).stop(removeId);
        verify(nodeLifecycle, times(1)).restart(eq(beforeId), any());
        verifyNoMoreInteractions(nodeLifecycle);
    }
}
