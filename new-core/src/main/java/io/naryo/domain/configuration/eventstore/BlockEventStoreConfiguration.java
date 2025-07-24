package io.naryo.domain.configuration.eventstore;

import java.util.EnumSet;
import java.util.Set;

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

    protected BlockEventStoreConfiguration(EventStoreType type, Set<EventStoreTarget> targets) {
        super(type, EventStoreStrategy.BLOCK_BASED);
        validateTargets(targets);
        this.targets = targets;
    }

    private void validateTargets(Set<EventStoreTarget> targets) {
        if (targets == null || targets.isEmpty()) {
            return;
        }

        EnumSet<TargetType> seenTypes = EnumSet.noneOf(TargetType.class);

        for (EventStoreTarget target : targets) {
            if (!seenTypes.add(target.type())) {
                throw new IllegalArgumentException(
                        "Duplicate TargetType detected: " + target.type());
            }
        }
    }
}
