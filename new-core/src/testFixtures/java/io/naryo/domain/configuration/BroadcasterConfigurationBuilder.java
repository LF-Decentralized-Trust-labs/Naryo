package io.naryo.domain.configuration;

import java.util.UUID;

import io.naryo.domain.DomainBuilder;
import io.naryo.domain.broadcaster.BroadcasterType;
import io.naryo.domain.configuration.broadcaster.BroadcasterCache;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

import static org.instancio.Select.field;

public abstract class BroadcasterConfigurationBuilder<
                T extends BroadcasterConfigurationBuilder<T, Y>, Y extends BroadcasterConfiguration>
        implements DomainBuilder<T, Y> {

    private UUID id;
    private BroadcasterType type;
    private BroadcasterCache cache;

    @Override
    public abstract T self();

    @Override
    public abstract Y build();

    public T withId(UUID id) {
        this.id = id;
        return self();
    }

    public T withType(BroadcasterType type) {
        this.type = type;
        return self();
    }

    public T withCache(BroadcasterCache cache) {
        this.cache = cache;
        return self();
    }

    protected UUID getId() {
        return this.id == null ? UUID.randomUUID() : this.id;
    }

    protected BroadcasterType getType() {
        return this.type == null ? Instancio.create(BroadcasterType.class) : this.type;
    }

    protected BroadcasterCache getCache() {
        return this.cache == null ? Instancio.create(BroadcasterCache.class) : this.cache;
    }

    protected InstancioApi<Y> buildBase(InstancioApi<Y> builder) {
        return builder.set(field(BroadcasterConfiguration::getId), this.getId())
                .set(field(BroadcasterConfiguration::getType), this.getType())
                .set(field(BroadcasterConfiguration::getCache), this.getCache());
    }
}
