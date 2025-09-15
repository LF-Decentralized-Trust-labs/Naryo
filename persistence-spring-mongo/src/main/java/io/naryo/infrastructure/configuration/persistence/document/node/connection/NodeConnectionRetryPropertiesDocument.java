package io.naryo.infrastructure.configuration.persistence.document.node.connection;

import java.time.Duration;
import java.util.Optional;

import io.naryo.application.configuration.source.model.node.connection.retry.NodeConnectionRetryDescriptor;
import jakarta.annotation.Nullable;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@TypeAlias("connection_retry")
public final class NodeConnectionRetryPropertiesDocument implements NodeConnectionRetryDescriptor {

    private @Setter @Nullable Integer times;
    private @Setter @Nullable Duration backoff;

    public NodeConnectionRetryPropertiesDocument(Integer times, Duration backoff) {
        this.times = times;
        this.backoff = backoff;
    }

    @Override
    public Optional<Integer> getTimes() {
        return Optional.ofNullable(times);
    }

    @Override
    public Optional<Duration> getBackoff() {
        return Optional.ofNullable(backoff);
    }
}
