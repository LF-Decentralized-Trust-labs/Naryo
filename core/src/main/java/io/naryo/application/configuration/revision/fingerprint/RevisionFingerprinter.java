package io.naryo.application.configuration.revision.fingerprint;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Function;

public interface RevisionFingerprinter<T> {

    String itemHash(T item);

    String revisionHash(Collection<T> items, Function<T, UUID> idFn);
}
