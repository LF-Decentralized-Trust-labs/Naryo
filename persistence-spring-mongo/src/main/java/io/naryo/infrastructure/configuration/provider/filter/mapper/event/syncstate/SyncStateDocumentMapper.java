package io.naryo.infrastructure.configuration.provider.filter.mapper.event.syncstate;

import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.filter.event.SyncState;
import io.naryo.domain.filter.event.sync.NoSyncState;
import io.naryo.domain.filter.event.sync.block.BlockActiveSyncState;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.syncstate.BlockActiveSyncStatePropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.syncstate.SyncStatePropertiesDocument;

public abstract class SyncStateDocumentMapper {

    public static SyncState fromDocument(SyncStatePropertiesDocument props) {
        if (props == null) {
            return new NoSyncState();
        }

        if (props instanceof BlockActiveSyncStatePropertiesDocument blockActiveSyncStatePropertiesDocument) {
            return new BlockActiveSyncState(
                new NonNegativeBlockNumber(blockActiveSyncStatePropertiesDocument.getInitialBlock()),
                new NonNegativeBlockNumber(blockActiveSyncStatePropertiesDocument.getLastBlockProcessed())
            );
        }

        throw new IllegalStateException("Unsupported sync state: " + props.getClass());
    }
}
