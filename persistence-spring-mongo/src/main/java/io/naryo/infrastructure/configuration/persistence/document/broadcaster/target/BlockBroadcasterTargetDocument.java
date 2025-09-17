package io.naryo.infrastructure.configuration.persistence.document.broadcaster.target;

import java.util.Set;

import io.naryo.application.configuration.source.model.broadcaster.target.BlockBroadcasterTargetDescriptor;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("block_broadcaster_targets")
public class BlockBroadcasterTargetDocument extends BroadcasterTargetDocument
        implements BlockBroadcasterTargetDescriptor {
    public BlockBroadcasterTargetDocument(Set<String> destinations) {
        super(destinations);
    }
}
