package io.naryo.application.configuration.revision;

public interface LiveRegistry<T> {

    Revision<T> active();

    void refresh(Revision<T> newRevision);

    default boolean isInitialized() {
        return active() != null;
    }
}
