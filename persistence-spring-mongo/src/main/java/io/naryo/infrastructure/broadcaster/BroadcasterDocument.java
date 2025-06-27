package io.naryo.infrastructure.broadcaster;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.UUID;

@Document(collection = "broadcasters")
@Getter
public class BroadcasterDocument {
    @MongoId
    private UUID id;
    private BroadcasterTargetDocument target;
    private UUID configurationId;
}
