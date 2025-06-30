package io.naryo.infrastructure.persistance.document.broadcaster;

import io.naryo.domain.broadcaster.BroadcasterTargetType;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "broadcaster_targets")
@Getter
public class BroadcasterTargetDocument {
    private BroadcasterTargetType type;
    private String destination;
    private UUID filterId;
}
