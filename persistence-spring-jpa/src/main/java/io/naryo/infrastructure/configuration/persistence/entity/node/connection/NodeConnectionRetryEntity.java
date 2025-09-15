package io.naryo.infrastructure.configuration.persistence.entity.node.connection;

import java.time.Duration;
import java.util.Optional;

import io.naryo.application.configuration.source.model.node.connection.retry.NodeConnectionRetryDescriptor;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
public class NodeConnectionRetryEntity implements NodeConnectionRetryDescriptor {

    private @Setter @Nullable @Column(name = "times") Integer times;
    private @Setter @Nullable @Column(name = "backoff") Duration backoff;

    public NodeConnectionRetryEntity(@Nullable Integer times, @Nullable Duration backoff) {
        this.times = times;
        this.backoff = backoff;
    }

    @Override
    public Optional<Integer> getTimes() {
        return Optional.ofNullable(this.times);
    }

    @Override
    public Optional<Duration> getBackoff() {
        return Optional.ofNullable(this.backoff);
    }
}
