package io.naryo.infrastructure.persistance.document.broadcaster;


import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Duration;
import java.util.UUID;

@Document(collection = "broadcaster_configurations")
@Getter
public class BroadcasterConfigurationDocument {
    @MongoId
    private UUID id;
    private String type;
    private Duration cache = Duration.ofMinutes(5);
    private Document additionalConfiguration;
}
