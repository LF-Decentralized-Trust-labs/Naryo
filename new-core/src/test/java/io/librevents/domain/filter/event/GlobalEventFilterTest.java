package io.librevents.domain.filter.event;

import java.util.List;
import java.util.UUID;

import io.librevents.domain.filter.FilterName;

import static org.junit.jupiter.api.Assertions.*;

class GlobalEventFilterTest extends AbstractEventFilterTest {

    @Override
    protected EventFilter createEventFilter(
            UUID id,
            FilterName name,
            UUID nodeId,
            EventFilterSpecification specification,
            List<EventStatus> statuses,
            SyncState syncState) {
        return new GlobalEventFilter(id, name, nodeId, specification, statuses, syncState);
    }
}
