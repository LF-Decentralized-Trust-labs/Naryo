package io.naryo.domain.filter.event;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.common.event.EventName;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.event.parameter.BoolParameterDefinition;
import io.naryo.domain.filter.event.sync.NoFilterSyncState;
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
            FilterSyncState filterSyncState) {
        return new ContractEventFilter(
                id, name, nodeId, specification, statuses, filterSyncState, "0x0");
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
                                new NoFilterSyncState(),
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
                                new NoFilterSyncState(),
                                ""));
    }
}
