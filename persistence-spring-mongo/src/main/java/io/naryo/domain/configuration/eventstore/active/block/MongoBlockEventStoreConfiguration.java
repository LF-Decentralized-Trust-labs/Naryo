package io.naryo.domain.configuration.eventstore.active.block;

import java.util.Set;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class MongoBlockEventStoreConfiguration extends BlockEventStoreConfiguration {

    public MongoBlockEventStoreConfiguration(UUID nodeId, Set<EventStoreTarget> targets) {
        super(nodeId, () -> "mongo", targets);
    }
}
