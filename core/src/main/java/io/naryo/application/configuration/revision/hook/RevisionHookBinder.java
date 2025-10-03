package io.naryo.application.configuration.revision.hook;

public interface RevisionHookBinder {

    void bindDefaults();

    <T> void register(Class<T> domainClass, RevisionHook<T> hook);
}
