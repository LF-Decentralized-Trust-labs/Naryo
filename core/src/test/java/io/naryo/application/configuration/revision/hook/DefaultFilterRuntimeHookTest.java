package io.naryo.application.configuration.revision.hook;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

import io.naryo.application.configuration.resilence.ResilienceRegistry;
import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.diff.DiffResult;
import io.naryo.application.configuration.revision.registry.LiveRegistry;
import io.naryo.application.event.decoder.ContractEventParameterDecoder;
import io.naryo.application.node.NodeLifecycle;
import io.naryo.application.node.NodeRunner;
import io.naryo.application.node.dispatch.Dispatcher;
import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.application.node.subscription.Subscriber;
import io.naryo.application.store.Store;
import io.naryo.application.store.filter.FilterStore;
import io.naryo.application.store.filter.model.FilterState;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.configuration.store.StoreState;
import io.naryo.domain.configuration.store.active.ActiveStoreConfiguration;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.event.EventFilter;
import io.naryo.domain.filter.event.GlobalEventFilterBuilder;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.subscription.SubscriptionStrategy;
import io.naryo.domain.node.subscription.block.BlockSubscriptionConfiguration;
import io.reactivex.disposables.Disposable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultFilterRuntimeHookTest {

    private @Mock NodeLifecycle nodeLifecycle;
    private @Mock ContractEventParameterDecoder decoder;
    private @Mock LiveRegistry<StoreConfiguration> storeLiveRegistry;
    private final ResilienceRegistry resilienceRegistry = new ResilienceRegistry();

    @SuppressWarnings("rawtypes")
    private @Mock FilterStore filterStore;

    private DefaultFilterRuntimeHook hook;

    @BeforeEach
    void setUp() {
        hook =
                new DefaultFilterRuntimeHook(
                        nodeLifecycle,
                        decoder,
                        resilienceRegistry,
                        storeLiveRegistry,
                        Set.<Store<?, ?, ?>>of(filterStore));
    }

    @Test
    void onAfterApply_whenNoAddedFilters_doesNothing() throws Exception {
        Revision<Filter> revision = mock();
        DiffResult<Filter> diff = mock();
        when(diff.added()).thenReturn(List.of());

        assertDoesNotThrow(() -> hook.onAfterApply(revision, diff));
        verifyNoInteractions(nodeLifecycle);
    }

    @Test
    void onAfterApply_whenAddedBlockBasedEventFilter_triggersSynchronization() throws Exception {
        UUID nodeId = UUID.randomUUID();
        Node node = mockBlockBasedNode(nodeId);
        NodeRunner runner = mockRunnerForNode(nodeId, node);
        Subscriber subscriber = mockSubscriberWithBlockInteractor(runner);
        ActiveStoreConfiguration activeStore = mockActiveStoreForNode(nodeId);
        mockStoreLiveRegistryWith(activeStore);

        EventFilter filter = new GlobalEventFilterBuilder().withNodeId(nodeId).build();
        FilterState filterState = mock();

        // noinspection unchecked
        when(filterStore.supports(any(), any())).thenReturn(true);

        //noinspection unchecked
        when(((FilterStore<ActiveStoreConfiguration>) filterStore)
                        .get(eq(activeStore), eq(filter.getId())))
                .thenReturn(Optional.of(filterState));

        DiffResult<Filter> diff = diffWithAdded(filter);
        Revision<Filter> revision = mock();

        ArgumentCaptor<Disposable> workflowCaptor = ArgumentCaptor.forClass(Disposable.class);
        doNothing().when(runner).addWorkflow(workflowCaptor.capture());

        assertDoesNotThrow(() -> hook.onAfterApply(revision, diff));

        verify(nodeLifecycle).getRunner(nodeId);
        verify(subscriber).getInteractor();
        verify(runner, times(1)).addWorkflow(any());
        verify(storeLiveRegistry).active();
        //noinspection unchecked
        verify((FilterStore<ActiveStoreConfiguration>) filterStore)
                .get(activeStore, filter.getId());
    }

    private Node mockBlockBasedNode(UUID nodeId) {
        Node node = mock();
        BlockSubscriptionConfiguration subConfig = mock();
        when(subConfig.getInitialBlock()).thenReturn(BigInteger.ZERO);
        when(node.getSubscriptionConfiguration()).thenReturn(subConfig);
        when(subConfig.getStrategy()).thenReturn(SubscriptionStrategy.BLOCK_BASED);
        when(node.getId()).thenReturn(nodeId);
        return node;
    }

    private NodeRunner mockRunnerForNode(UUID nodeId, Node node) {
        NodeRunner runner = mock();
        when(nodeLifecycle.getRunner(eq(nodeId))).thenReturn(runner);
        when(runner.getNode()).thenReturn(node);
        when(runner.getSubscriber()).thenReturn(mock(Subscriber.class));
        when(runner.getDispatcher()).thenReturn(mock(Dispatcher.class));
        return runner;
    }

    private Subscriber mockSubscriberWithBlockInteractor(NodeRunner runner) throws IOException {
        Subscriber subscriber = mock(Subscriber.class);
        when(runner.getSubscriber()).thenReturn(subscriber);
        BlockInteractor interactor = mock(BlockInteractor.class);
        when(subscriber.getInteractor()).thenReturn(interactor);
        when(interactor.getLogs(any(), any(), anyList())).thenReturn(List.of());
        return subscriber;
    }

    private ActiveStoreConfiguration mockActiveStoreForNode(UUID nodeId) {
        ActiveStoreConfiguration activeStore = mock(ActiveStoreConfiguration.class);
        when(activeStore.getNodeId()).thenReturn(nodeId);
        when(activeStore.getState()).thenReturn(StoreState.ACTIVE);
        return activeStore;
    }

    private void mockStoreLiveRegistryWith(StoreConfiguration conf) {
        Revision<StoreConfiguration> revision = mock();
        when(storeLiveRegistry.active()).thenReturn(revision);
        when(revision.domainItems()).thenReturn(List.of(conf));
    }

    private EventFilter mockEventFilter(UUID nodeId) {
        EventFilter filter = mock(EventFilter.class);
        when(filter.getId()).thenReturn(UUID.randomUUID());
        when(filter.getNodeId()).thenReturn(nodeId);
        return filter;
    }

    private DiffResult<Filter> diffWithAdded(Filter filter) {
        return new DiffResult<>(List.of(filter), List.of(), List.of());
    }
}
