package io.naryo.domain.configuration.broadcaster;

import java.util.UUID;

import io.naryo.domain.DomainBuilder;
import org.instancio.Instancio;

public abstract class BroadcasterConfigurationBuilder<
                T extends BroadcasterConfigurationBuilder<T, Y>, Y extends BroadcasterConfiguration>
        implements DomainBuilder<T, Y> {

    private UUID id;
    private BroadcasterCache cache;

    @Override
    public abstract T self();

    @Override
    public abstract Y build();

    public T withId(UUID id) {
        this.id = id;
        return self();
    }

    public T withCache(BroadcasterCache cache) {
        this.cache = cache;
        return self();
    }

    protected UUID getId() {
        return this.id == null ? UUID.randomUUID() : this.id;
    }

    protected BroadcasterCache getCache() {
        return this.cache == null ? Instancio.create(BroadcasterCache.class) : this.cache;
    }
}
