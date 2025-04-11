package io.librevents.domain.filter.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import io.librevents.domain.filter.FilterName;
import io.librevents.domain.filter.event.parameter.BoolParameterDefinition;
import io.librevents.domain.filter.event.sync.NoSyncState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

abstract class AbstractEventFilterTest {

    protected abstract EventFilter createEventFilter(
            UUID id,
            FilterName name,
            UUID nodeId,
            EventFilterSpecification specification,
            List<EventStatus> statuses,
            SyncState syncState);

    @Test
    void testValidEventFilter() {
        UUID id = UUID.randomUUID();
        FilterName name = new FilterName("Test Filter");
        UUID nodeId = UUID.randomUUID();
        Set<ParameterDefinition> parameters = Set.of(new BoolParameterDefinition(0, false));
        EventFilterSpecification specification =
                new EventFilterSpecification(
                        new EventName("Test"), new CorrelationId(0), parameters);
        List<EventStatus> statuses = List.of(EventStatus.CONFIRMED);
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
        List<EventStatus> statuses = List.of(EventStatus.CONFIRMED);
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
        List<EventStatus> statuses = new ArrayList<>();
        SyncState syncState = new NoSyncState();

        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    createEventFilter(id, name, nodeId, specification, statuses, syncState);
                });
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
                new EventFilterSpecification(new EventName("Test"), new CorrelationId(0), parameters);
        List<EventStatus> statuses = List.of(EventStatus.CONFIRMED);

        assertThrows(
                NullPointerException.class,
                () -> {
                    createEventFilter(id, name, nodeId, specification, statuses, null);
                });
    }
}
