package io.naryo.application.configuration.revision.hook;

import java.util.List;

import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.diff.DiffResult;

public interface RevisionHook<T> {

    default void onBeforeApply(List<T> oldDomain, List<T> newDomain) throws Exception {}

    default void onAfterApply(Revision<T> applied, DiffResult<T> diff) throws Exception {}
}
