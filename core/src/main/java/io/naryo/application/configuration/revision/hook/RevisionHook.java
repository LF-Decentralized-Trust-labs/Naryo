package io.naryo.application.configuration.revision.hook;

import java.util.Collection;

import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.diff.DiffResult;

public interface RevisionHook<T> {

    default void onBeforeApply(Collection<T> oldDomain, Collection<T> newDomain) throws Exception {}

    default void onAfterApply(Revision<T> applied, DiffResult<T> diff) throws Exception {}
}
