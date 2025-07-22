package io.naryo.infrastructure.configuration.persistence.document.broadcaster.target;

import io.naryo.application.configuration.source.model.broadcaster.target.AllBroadcasterTargetDescriptor;
import io.naryo.domain.broadcaster.BroadcasterTargetType;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "broadcasters")
@TypeAlias("all_broadcaster_targets")
public class AllBroadcasterTargetDocument extends BroadcasterTargetDocument
        implements AllBroadcasterTargetDescriptor {

    public AllBroadcasterTargetDocument(String destination) {
        super(BroadcasterTargetType.ALL, destination);
    }
}
