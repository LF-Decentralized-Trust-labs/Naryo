package io.naryo.domain.broadcaster;

import java.util.Objects;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class Broadcaster {

    private final UUID id;
    private BroadcasterTarget target;
    private UUID configurationId;

    public Broadcaster(UUID id, BroadcasterTarget target, UUID configurationId) {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(target, "target cannot be null");
        Objects.requireNonNull(configurationId, "configurationId cannot be null");

        this.id = id;
        this.target = target;
        this.configurationId = configurationId;
    }

    public Broadcaster merge(Broadcaster other) {
        if (other == null) {
            return this;
        }
        if (!this.id.equals(other.id)) {
            throw new IllegalArgumentException("Cannot merge broadcasters with different IDs");
        }
        this.target = other.target != null ? other.target : this.target;
        this.configurationId =
                other.configurationId != null ? other.configurationId : this.configurationId;
        return this;
    }
}
