package io.naryo.domain.configuration.eventstore;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

import io.naryo.domain.configuration.eventstore.block.EventStoreTarget;
import io.naryo.domain.configuration.eventstore.block.TargetType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class BlockEventStoreConfiguration extends EventStoreConfiguration {

    protected final Set<EventStoreTarget> targets;

    protected BlockEventStoreConfiguration(
            UUID nodeId, EventStoreType type, Set<EventStoreTarget> targets) {
        super(nodeId, type, EventStoreStrategy.BLOCK_BASED);
        validateTargets(targets);
        this.targets = targets;
    }

    private void validateTargets(Set<EventStoreTarget> targets) {
        if (targets == null || targets.isEmpty()) {
            throw new IllegalArgumentException("Targets cannot be null or empty");
        }

        EnumSet<TargetType> seenTypes = EnumSet.noneOf(TargetType.class);

        for (EventStoreTarget target : targets) {
            if (!seenTypes.add(target.type())) {
                throw new IllegalArgumentException(
                        "Duplicate TargetType detected: " + target.type());
            }
        }

        EnumSet<TargetType> allTypes = EnumSet.allOf(TargetType.class);
        if (!seenTypes.equals(allTypes)) {
            Set<TargetType> missingTypes = EnumSet.copyOf(allTypes);
            missingTypes.removeAll(seenTypes);
            throw new IllegalArgumentException("Missing required TargetTypes: " + missingTypes);
        }
    }
}
