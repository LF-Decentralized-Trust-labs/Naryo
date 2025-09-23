package io.naryo.application.common.revision;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public interface RevisionFingerprinter<T> {

    String itemHash(T item);

    String revisionHash(List<T> items, Function<T, UUID> idFn);
}
