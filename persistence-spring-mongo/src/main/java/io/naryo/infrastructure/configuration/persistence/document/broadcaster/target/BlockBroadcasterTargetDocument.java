package io.naryo.infrastructure.configuration.persistence.document.broadcaster.target;

import io.naryo.application.configuration.source.model.broadcaster.target.BlockBroadcasterTargetDescriptor;
import io.naryo.domain.broadcaster.BroadcasterTargetType;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "broadcasters")
@TypeAlias("block_broadcaster_targets")
public class BlockBroadcasterTargetDocument extends BroadcasterTargetDocument implements BlockBroadcasterTargetDescriptor {
    public BlockBroadcasterTargetDocument(String destination) {
        super(BroadcasterTargetType.BLOCK, destination);
    }

    public BlockBroadcasterTargetDocument() {
        super(BroadcasterTargetType.BLOCK, null);
    }
}
