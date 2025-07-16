package io.naryo.infrastructure.configuration.provider.filter.mapper.event.syncstate;

import fixtures.persistence.filter.event.BlockActiveSyncStateDocumentBuilder;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.filter.event.BlockActiveSyncStateBuilder;
import io.naryo.domain.filter.event.SyncState;
import io.naryo.domain.filter.event.sync.NoSyncState;
import io.naryo.domain.filter.event.sync.block.BlockActiveSyncState;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.syncstate.BlockActiveSyncStateDocument;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class SyncStateDocumentMapperTest {

    @Test
    void testFromDocumentForNoSyncState() {
        SyncState expected = new NoSyncState();

        SyncState result = SyncStateDocumentMapper.fromDocument(null);

        assertEquals(expected, result);
    }

    @Test
    void testFromDocumentForBlockActiveSyncState() {
        BlockActiveSyncStateDocument document = new BlockActiveSyncStateDocumentBuilder().build();
        BlockActiveSyncState expected = new BlockActiveSyncStateBuilder()
            .withInitialBlock(new NonNegativeBlockNumber(document.getInitialBlock()))
            .withLastBlockProcessed(new NonNegativeBlockNumber(document.getLastBlockProcessed()))
            .build();

        SyncState result = SyncStateDocumentMapper.fromDocument(document);

        assertEquals(expected, result);
    }
}
