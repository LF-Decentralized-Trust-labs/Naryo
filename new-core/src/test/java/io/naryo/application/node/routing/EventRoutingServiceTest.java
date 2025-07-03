package io.naryo.application.node.routing;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.broadcaster.BroadcasterTargetType;
import io.naryo.domain.broadcaster.Destination;
import io.naryo.domain.broadcaster.target.FilterEventBroadcasterTarget;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.common.event.EventName;
import io.naryo.domain.event.Event;
import io.naryo.domain.event.EventType;
import io.naryo.domain.event.contract.ContractEvent;
import io.naryo.domain.event.contract.parameter.IntParameter;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.FilterType;
import io.naryo.domain.filter.event.EventFilter;
import io.naryo.domain.filter.event.EventFilterSpecification;
import io.naryo.domain.filter.event.ParameterDefinition;
import io.naryo.domain.filter.event.parameter.IntParameterDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventRoutingServiceTest {

    private List<Filter> filters = new ArrayList<>();
    @Mock private Broadcaster wrapperAll;
    @Mock private Broadcaster wrapperBlock;
    @Mock private Broadcaster wrapperTx;
    @Mock private Broadcaster wrapperContractEvent;
    @Mock private Broadcaster wrapperFilter;

    private EventRoutingService service;

    @BeforeEach
    void setUp() {
        service = new EventRoutingService(filters);
    }

    @Test
    void matchingWrappers_blockEvent_filtersByBlockAndAll() {
        when(wrapperAll.getTarget()).thenReturn(new DummyTarget(BroadcasterTargetType.ALL));
        when(wrapperBlock.getTarget()).thenReturn(new DummyTarget(BroadcasterTargetType.BLOCK));
        when(wrapperTx.getTarget()).thenReturn(new DummyTarget(BroadcasterTargetType.TRANSACTION));

        Event block = mock(Event.class);
        when(block.getEventType()).thenReturn(EventType.BLOCK);

        List<Broadcaster> wrappers = List.of(wrapperAll, wrapperBlock, wrapperTx);
        List<Broadcaster> result = service.matchingWrappers(block, wrappers);

        assertTrue(result.contains(wrapperAll), "ALL should always be included");
        assertTrue(result.contains(wrapperBlock), "BLOCK should be included for BLOCK events");
        assertFalse(
                result.contains(wrapperTx), "TRANSACTION should NOT be included for BLOCK events");
    }

    @Test
    void matchingWrappers_transactionEvent_filtersByTxAndAll() {
        when(wrapperAll.getTarget()).thenReturn(new DummyTarget(BroadcasterTargetType.ALL));
        when(wrapperBlock.getTarget()).thenReturn(new DummyTarget(BroadcasterTargetType.BLOCK));
        when(wrapperTx.getTarget()).thenReturn(new DummyTarget(BroadcasterTargetType.TRANSACTION));

        Event tx = mock(Event.class);
        when(tx.getEventType()).thenReturn(EventType.TRANSACTION);

        List<Broadcaster> wrappers = List.of(wrapperAll, wrapperBlock, wrapperTx);
        List<Broadcaster> result = service.matchingWrappers(tx, wrappers);

        assertTrue(result.contains(wrapperAll));
        assertTrue(result.contains(wrapperTx));
        assertFalse(result.contains(wrapperBlock));
    }

    @Test
    void matchingWrappers_contractEvent_includesContractAndFilterMatches() {
        when(wrapperAll.getTarget()).thenReturn(new DummyTarget(BroadcasterTargetType.ALL));
        when(wrapperContractEvent.getTarget())
                .thenReturn(new DummyTarget(BroadcasterTargetType.CONTRACT_EVENT));
        UUID filterId = UUID.randomUUID();
        var filterTarget = new FilterEventBroadcasterTarget(new Destination("d"), filterId);
        when(wrapperFilter.getTarget()).thenReturn(filterTarget);

        EventFilter evtFilter = mock(EventFilter.class);
        when(evtFilter.getType()).thenReturn(FilterType.EVENT);
        when(evtFilter.getId()).thenReturn(filterId);

        var spec = mock(EventFilterSpecification.class);
        when(spec.eventName()).thenReturn(new EventName("Foo"));
        ParameterDefinition pd = new IntParameterDefinition(256);
        when(spec.parameters()).thenReturn(Set.of(pd));
        when(evtFilter.getSpecification()).thenReturn(spec);
        when(evtFilter.getStatuses()).thenReturn(List.of(ContractEventStatus.CONFIRMED));

        filters.add(evtFilter);

        ContractEvent ce = mock(ContractEvent.class);
        when(ce.getEventType()).thenReturn(EventType.CONTRACT);
        when(ce.getName()).thenReturn(new EventName("Foo"));
        when(ce.getStatus()).thenReturn(ContractEventStatus.CONFIRMED);
        IntParameter param = new IntParameter(false, 0, 42);
        when(ce.getParameters()).thenReturn(Set.of(param));

        List<Broadcaster> wrappers = List.of(wrapperAll, wrapperContractEvent, wrapperFilter);
        List<Broadcaster> result = service.matchingWrappers(ce, wrappers);

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
