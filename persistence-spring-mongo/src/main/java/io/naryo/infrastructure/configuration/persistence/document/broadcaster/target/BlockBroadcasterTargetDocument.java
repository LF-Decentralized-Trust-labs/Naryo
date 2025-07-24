package io.naryo.infrastructure.configuration.persistence.document.broadcaster.target;

import io.naryo.application.configuration.source.model.broadcaster.target.BlockBroadcasterTargetDescriptor;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("block_broadcaster_targets")
public class BlockBroadcasterTargetDocument extends BroadcasterTargetDocument
        implements BlockBroadcasterTargetDescriptor {
    public BlockBroadcasterTargetDocument(String destination) {
        super(destination);
    }
}
