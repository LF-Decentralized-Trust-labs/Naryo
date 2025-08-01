package io.naryo.infrastructure.configuration.persistence.document.broadcaster.target;

import io.naryo.application.configuration.source.model.broadcaster.target.AllBroadcasterTargetDescriptor;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("all_broadcaster_targets")
public class AllBroadcasterTargetDocument extends BroadcasterTargetDocument
        implements AllBroadcasterTargetDescriptor {

    public AllBroadcasterTargetDocument(String destination) {
        super(destination);
    }
}
