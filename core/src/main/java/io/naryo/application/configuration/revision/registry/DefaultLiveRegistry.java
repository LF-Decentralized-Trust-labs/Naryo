package io.naryo.application.configuration.revision.registry;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import io.naryo.application.configuration.revision.Revision;

public class DefaultLiveRegistry<T> implements LiveRegistry<T> {

    private final AtomicReference<Revision<T>> holder = new AtomicReference<>();

    @Override
    public Revision<T> active() {
        return holder.get();
    }

    @Override
    public void refresh(Revision<T> newRevision) {
        Objects.requireNonNull(newRevision, "newRevision must not be null");
        holder.set(newRevision);
    }
}
