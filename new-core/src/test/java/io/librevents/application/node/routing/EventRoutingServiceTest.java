package io.librevents.application.node.routing;

import java.util.*;

import io.librevents.application.broadcaster.BroadcasterWrapper;
import io.librevents.domain.broadcaster.Broadcaster;
import io.librevents.domain.broadcaster.BroadcasterTarget;
import io.librevents.domain.broadcaster.BroadcasterTargetType;
import io.librevents.domain.broadcaster.Destination;
import io.librevents.domain.broadcaster.target.FilterEventBroadcasterTarget;
import io.librevents.domain.common.ContractEventStatus;
import io.librevents.domain.common.EventName;
import io.librevents.domain.event.Event;
import io.librevents.domain.event.EventType;
import io.librevents.domain.event.contract.ContractEvent;
import io.librevents.domain.event.contract.parameter.IntParameter;
import io.librevents.domain.filter.Filter;
import io.librevents.domain.filter.FilterRepository;
import io.librevents.domain.filter.FilterType;
import io.librevents.domain.filter.event.EventFilter;
import io.librevents.domain.filter.event.EventFilterSpecification;
import io.librevents.domain.filter.event.ParameterDefinition;
import io.librevents.domain.filter.event.parameter.IntParameterDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventRoutingServiceTest {

    @Mock private FilterRepository repository;
    @Mock private BroadcasterWrapper wrapperAll;
    @Mock private BroadcasterWrapper wrapperBlock;
    @Mock private BroadcasterWrapper wrapperTx;
    @Mock private BroadcasterWrapper wrapperContractEvent;
    @Mock private BroadcasterWrapper wrapperFilter;

    private EventRoutingService service;

    @BeforeEach
    void setUp() {
        service = new EventRoutingService(repository);
    }

    @Test
    void getAllFilters_shouldLoadOnceAndCache() {
        var bcFilter = mock(Broadcaster.class);
        UUID filterId = UUID.randomUUID();
        var filterTarget = new FilterEventBroadcasterTarget(new Destination("dest"), filterId);
        when(wrapperFilter.broadcaster()).thenReturn(bcFilter);
        when(bcFilter.target()).thenReturn(filterTarget);

        var bcAll = mock(Broadcaster.class);
        when(wrapperAll.broadcaster()).thenReturn(bcAll);
        when(bcAll.target()).thenReturn(new DummyTarget(BroadcasterTargetType.ALL));

        var bcBlock = mock(Broadcaster.class);
        when(wrapperBlock.broadcaster()).thenReturn(bcBlock);
        when(bcBlock.target()).thenReturn(new DummyTarget(BroadcasterTargetType.BLOCK));

        Filter f1 = mock(Filter.class);
        when(repository.findAllById(List.of(filterId))).thenReturn(List.of(f1));

        List<Filter> first =
                service.getAllFilters(List.of(wrapperAll, wrapperFilter, wrapperBlock));
        assertEquals(1, first.size());
        assertSame(f1, first.getFirst());
        verify(repository, times(1)).findAllById(List.of(filterId));

        List<Filter> second = service.getAllFilters(Collections.emptyList());
        assertSame(first, second);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void matchingWrappers_blockEvent_filtersByBlockAndAll() {
        var bcAll = mock(Broadcaster.class);
        when(wrapperAll.broadcaster()).thenReturn(bcAll);
        when(bcAll.target()).thenReturn(new DummyTarget(BroadcasterTargetType.ALL));

        var bcBlock = mock(Broadcaster.class);
        when(wrapperBlock.broadcaster()).thenReturn(bcBlock);
        when(bcBlock.target()).thenReturn(new DummyTarget(BroadcasterTargetType.BLOCK));

        var bcTx = mock(Broadcaster.class);
        when(wrapperTx.broadcaster()).thenReturn(bcTx);
        when(bcTx.target()).thenReturn(new DummyTarget(BroadcasterTargetType.TRANSACTION));

        Event block = mock(Event.class);
        when(block.getEventType()).thenReturn(EventType.BLOCK);

        List<BroadcasterWrapper> wrappers = List.of(wrapperAll, wrapperBlock, wrapperTx);
        List<BroadcasterWrapper> result = service.matchingWrappers(block, wrappers);

        assertTrue(result.contains(wrapperAll), "ALL should always be included");
        assertTrue(result.contains(wrapperBlock), "BLOCK should be included for BLOCK events");
        assertFalse(
                result.contains(wrapperTx), "TRANSACTION should NOT be included for BLOCK events");
    }

    @Test
    void matchingWrappers_transactionEvent_filtersByTxAndAll() {
        var bcAll = mock(Broadcaster.class);
        when(wrapperAll.broadcaster()).thenReturn(bcAll);
        when(bcAll.target()).thenReturn(new DummyTarget(BroadcasterTargetType.ALL));

        var bcBlock = mock(Broadcaster.class);
        when(wrapperBlock.broadcaster()).thenReturn(bcBlock);
        when(bcBlock.target()).thenReturn(new DummyTarget(BroadcasterTargetType.BLOCK));

        var bcTx = mock(Broadcaster.class);
        when(wrapperTx.broadcaster()).thenReturn(bcTx);
        when(bcTx.target()).thenReturn(new DummyTarget(BroadcasterTargetType.TRANSACTION));

        Event tx = mock(Event.class);
        when(tx.getEventType()).thenReturn(EventType.TRANSACTION);

        List<BroadcasterWrapper> wrappers = List.of(wrapperAll, wrapperBlock, wrapperTx);
        List<BroadcasterWrapper> result = service.matchingWrappers(tx, wrappers);

        assertTrue(result.contains(wrapperAll));
        assertTrue(result.contains(wrapperTx));
        assertFalse(result.contains(wrapperBlock));
    }

    @Test
    void matchingWrappers_contractEvent_includesContractAndFilterMatches() {
        var bcAll = mock(Broadcaster.class);
        when(wrapperAll.broadcaster()).thenReturn(bcAll);
        when(bcAll.target()).thenReturn(new DummyTarget(BroadcasterTargetType.ALL));

        var bcContract = mock(Broadcaster.class);
        when(wrapperContractEvent.broadcaster()).thenReturn(bcContract);
        when(bcContract.target()).thenReturn(new DummyTarget(BroadcasterTargetType.CONTRACT_EVENT));

        var bcFilter = mock(Broadcaster.class);
        UUID filterId = UUID.randomUUID();
        var filterTarget = new FilterEventBroadcasterTarget(new Destination("d"), filterId);
        when(wrapperFilter.broadcaster()).thenReturn(bcFilter);
        when(bcFilter.target()).thenReturn(filterTarget);

        EventFilter evtFilter = mock(EventFilter.class);
        when(evtFilter.getType()).thenReturn(FilterType.EVENT);
        when(evtFilter.getId()).thenReturn(filterId);

        var spec = mock(EventFilterSpecification.class);
        when(spec.eventName()).thenReturn(new EventName("Foo"));
        ParameterDefinition pd = new IntParameterDefinition(256);
        when(spec.parameters()).thenReturn(Set.of(pd));
        when(evtFilter.getSpecification()).thenReturn(spec);
        when(evtFilter.getStatuses()).thenReturn(List.of(ContractEventStatus.CONFIRMED));

        when(repository.findAllById(List.of(filterId))).thenReturn(List.of(evtFilter));

        ContractEvent ce = mock(ContractEvent.class);
        when(ce.getEventType()).thenReturn(EventType.CONTRACT);
        when(ce.getName()).thenReturn(new EventName("Foo"));
        when(ce.getStatus()).thenReturn(ContractEventStatus.CONFIRMED);
        IntParameter param = new IntParameter(false, 0, 42);
        when(ce.getParameters()).thenReturn(Set.of(param));

        List<BroadcasterWrapper> wrappers =
                List.of(wrapperAll, wrapperContractEvent, wrapperFilter);
        List<BroadcasterWrapper> result = service.matchingWrappers(ce, wrappers);

        assertTrue(result.contains(wrapperAll));
        assertTrue(result.contains(wrapperContractEvent));
        assertTrue(result.contains(wrapperFilter));
    }

    private static class DummyTarget extends BroadcasterTarget {
        DummyTarget(BroadcasterTargetType type) {
            super(type, new Destination("dummy"));
        }
    }
}
