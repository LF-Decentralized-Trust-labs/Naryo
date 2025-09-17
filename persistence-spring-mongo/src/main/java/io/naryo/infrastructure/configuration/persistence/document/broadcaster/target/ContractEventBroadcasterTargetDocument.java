package io.naryo.infrastructure.configuration.persistence.document.broadcaster.target;

import java.util.List;
import java.util.Set;

import io.naryo.application.configuration.source.model.broadcaster.target.ContractEventBroadcasterTargetDescriptor;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("contract_event_broadcaster_targets")
public class ContractEventBroadcasterTargetDocument extends BroadcasterTargetDocument
        implements ContractEventBroadcasterTargetDescriptor {
    public ContractEventBroadcasterTargetDocument(Set<String> destinations) {
        super(destinations);
    }
}
