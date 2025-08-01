package io.naryo.domain.filter.event;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.common.event.EventName;
import io.naryo.domain.filter.AbstractFilterTest;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.event.parameter.BoolParameterDefinition;
import io.naryo.domain.filter.event.sync.NoSyncState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract class AbstractEventFilterTest extends AbstractFilterTest {

    protected abstract EventFilter createEventFilter(
            UUID id,
            FilterName name,
            UUID nodeId,
            EventFilterSpecification specification,
            Set<ContractEventStatus> statuses,
            SyncState syncState);

    @Override
    protected Filter createFilter(UUID id, FilterName name, UUID nodeId) {
        return createEventFilter(
                id,
                name,
                nodeId,
                new EventFilterSpecificationBuilder().build(),
                new HashSet<>(),
                new BlockActiveSyncStateBuilder().build());
    }

    @Test
    void testValidEventFilter() {
        UUID id = UUID.randomUUID();
        FilterName name = new FilterName("Test Filter");
        UUID nodeId = UUID.randomUUID();
        Set<ParameterDefinition> parameters = Set.of(new BoolParameterDefinition(0, false));
        EventFilterSpecification specification =
                new EventFilterSpecification(
                        new EventName("Test"), new CorrelationId(0), parameters);
        Set<ContractEventStatus> statuses = Set.of(ContractEventStatus.CONFIRMED);
        SyncState syncState = new NoSyncState();

        EventFilter eventFilter =
                createEventFilter(id, name, nodeId, specification, statuses, syncState);

        assertEquals(id, eventFilter.getId());
        assertEquals(name, eventFilter.getName());
        assertEquals(nodeId, eventFilter.getNodeId());
        assertEquals(specification, eventFilter.getSpecification());
        assertEquals(statuses, eventFilter.getStatuses());
        assertEquals(syncState, eventFilter.getSyncState());
    }

    @Test
    void testNullSpecification() {
        UUID id = UUID.randomUUID();
        FilterName name = new FilterName("Test Filter");
        UUID nodeId = UUID.randomUUID();
        Set<ContractEventStatus> statuses = Set.of(ContractEventStatus.CONFIRMED);
        SyncState syncState = new NoSyncState();

        assertThrows(
                NullPointerException.class,
                () -> {
                    createEventFilter(id, name, nodeId, null, statuses, syncState);
                });
    }

    @Test
    void testEmptyStatuses() {
        UUID id = UUID.randomUUID();
        FilterName name = new FilterName("Test Filter");
        UUID nodeId = UUID.randomUUID();
        Set<ParameterDefinition> parameters = Set.of(new BoolParameterDefinition(0, false));
        EventFilterSpecification specification =
                new EventFilterSpecification(
                        new EventName("Test"), new CorrelationId(0), parameters);
        Set<ContractEventStatus> statuses = new HashSet<>();
        SyncState syncState = new NoSyncState();

        EventFilter eventFilter =
                createEventFilter(id, name, nodeId, specification, statuses, syncState);

        assertEquals(id, eventFilter.getId());
        assertEquals(name, eventFilter.getName());
        assertEquals(nodeId, eventFilter.getNodeId());
        assertEquals(specification, eventFilter.getSpecification());
        assertEquals(Set.of(ContractEventStatus.values()), eventFilter.getStatuses());
        assertEquals(syncState, eventFilter.getSyncState());
    }

    @Test
    void testNullStatuses() {
        UUID id = UUID.randomUUID();
        FilterName name = new FilterName("Test Filter");
        UUID nodeId = UUID.randomUUID();
        Set<ParameterDefinition> parameters = Set.of(new BoolParameterDefinition(0, false));
        EventFilterSpecification specification =
                new EventFilterSpecification(
                        new EventName("Test"), new CorrelationId(0), parameters);
        SyncState syncState = new NoSyncState();

        assertThrows(
                NullPointerException.class,
                () -> {
                    createEventFilter(id, name, nodeId, specification, null, syncState);
                });
    }

    @Test
    void testNullSyncState() {
        UUID id = UUID.randomUUID();
        FilterName name = new FilterName("Test Filter");
        UUID nodeId = UUID.randomUUID();
        Set<ParameterDefinition> parameters = Set.of(new BoolParameterDefinition(0, false));
        EventFilterSpecification specification =
                new EventFilterSpecification(
                        new EventName("Test"), new CorrelationId(0), parameters);
        Set<ContractEventStatus> statuses = Set.of(ContractEventStatus.CONFIRMED);

        assertThrows(
                NullPointerException.class,
                () -> {
                    createEventFilter(id, name, nodeId, specification, statuses, null);
                });
    }
}
