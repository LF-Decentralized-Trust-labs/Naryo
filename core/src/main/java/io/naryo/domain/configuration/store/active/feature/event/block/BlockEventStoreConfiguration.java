package io.naryo.domain.configuration.store.active.feature.event.block;

import java.util.EnumSet;
import java.util.Set;

import io.naryo.domain.configuration.store.active.feature.event.EventStoreConfiguration;
import io.naryo.domain.configuration.store.active.feature.event.EventStoreStrategy;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class BlockEventStoreConfiguration extends EventStoreConfiguration {

    private final Set<EventStoreTarget> targets;

    public BlockEventStoreConfiguration(Set<EventStoreTarget> targets) {
        super(EventStoreStrategy.BLOCK_BASED);
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
