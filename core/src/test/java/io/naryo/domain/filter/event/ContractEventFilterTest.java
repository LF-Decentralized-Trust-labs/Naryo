package io.naryo.domain.filter.event;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.common.event.EventName;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.event.parameter.BoolParameterDefinition;
import io.naryo.domain.filter.event.sync.NoSyncState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ContractEventFilterTest extends AbstractEventFilterTest {

    @Override
    protected EventFilter createEventFilter(
            UUID id,
            FilterName name,
            UUID nodeId,
            EventFilterSpecification specification,
            Set<ContractEventStatus> statuses,
            SyncState syncState) {
        return new ContractEventFilter(id, name, nodeId, specification, statuses, syncState, "0x0");
    }

    @Test
    void testNullContractAddress() {
        EventName name = new EventName("Test");
        assertThrows(
                NullPointerException.class,
                () ->
                        new ContractEventFilter(
                                UUID.randomUUID(),
                                new FilterName("Test"),
                                UUID.randomUUID(),
                                new EventFilterSpecification(
                                        name,
                                        new CorrelationId(0),
                                        Set.of(new BoolParameterDefinition(0, false))),
                                Set.of(ContractEventStatus.CONFIRMED),
                                new NoSyncState(),
                                null));
    }

    @Test
    void testEmptyContractAddress() {
        EventName name = new EventName("Test");
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        new ContractEventFilter(
                                UUID.randomUUID(),
                                new FilterName("Test"),
                                UUID.randomUUID(),
                                new EventFilterSpecification(
                                        name,
                                        new CorrelationId(0),
                                        Set.of(new BoolParameterDefinition(0, false))),
                                Set.of(ContractEventStatus.CONFIRMED),
                                new NoSyncState(),
                                ""));
    }
}
