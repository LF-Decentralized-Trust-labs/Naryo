package io.naryo.infrastructure.configuration.persistence.document.broadcaster.target;

import io.naryo.application.configuration.source.model.broadcaster.target.ContractEventBroadcasterTargetDescriptor;
import io.naryo.domain.broadcaster.BroadcasterTargetType;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "broadcasters")
@TypeAlias("contract_event_broadcaster_targets")
public class ContractEventBroadcasterTargetDocument extends BroadcasterTargetDocument
        implements ContractEventBroadcasterTargetDescriptor {
    public ContractEventBroadcasterTargetDocument(String destination) {
        super(BroadcasterTargetType.CONTRACT_EVENT, destination);
    }

    public ContractEventBroadcasterTargetDocument() {
        super(BroadcasterTargetType.CONTRACT_EVENT, null);
    }
}
