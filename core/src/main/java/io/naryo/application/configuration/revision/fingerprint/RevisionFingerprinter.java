package io.naryo.application.configuration.revision.fingerprint;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public interface RevisionFingerprinter<T> {

    String itemHash(T item);

    String revisionHash(List<T> items, Function<T, UUID> idFn);
}
