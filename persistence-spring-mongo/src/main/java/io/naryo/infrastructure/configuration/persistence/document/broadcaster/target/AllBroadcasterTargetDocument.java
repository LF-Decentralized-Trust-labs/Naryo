package io.naryo.infrastructure.configuration.persistence.document.broadcaster.target;

import java.util.List;
import java.util.Set;

import io.naryo.application.configuration.source.model.broadcaster.target.AllBroadcasterTargetDescriptor;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("all_broadcaster_targets")
public class AllBroadcasterTargetDocument extends BroadcasterTargetDocument
        implements AllBroadcasterTargetDescriptor {

    public AllBroadcasterTargetDocument(Set<String> destinations) {
        super(destinations);
    }
}
