package io.naryo.application.configuration.revision.manager;

import io.naryo.application.configuration.revision.LiveView;
import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.hook.RevisionHook;
import io.naryo.application.configuration.revision.operation.RevisionOperation;

public interface ConfigurationRevisionManager<T> {

    Revision<T> initialize() throws Exception;

    Revision<T> apply(RevisionOperation<T> op) throws Exception;

    void registerHook(RevisionHook<T> hook);

    LiveView<T> liveView();
}
