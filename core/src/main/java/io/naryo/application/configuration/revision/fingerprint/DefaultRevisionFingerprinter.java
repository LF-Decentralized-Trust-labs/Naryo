package io.naryo.application.configuration.revision.fingerprint;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.naryo.application.common.util.EncryptionUtil;
import io.naryo.domain.normalization.Normalizer;

public abstract class DefaultRevisionFingerprinter<T> implements RevisionFingerprinter<T> {

    private final Normalizer<T> normalizer;

    protected DefaultRevisionFingerprinter(Normalizer<T> normalizer) {
        this.normalizer = normalizer;
    }

    @Override
    public String itemHash(T item) {
        Objects.requireNonNull(item, "item cannot be null");
        T normalized = normalizer.normalize(item);
        String canonicalJson = CanonicalJson.write(normalized);
        return EncryptionUtil.sha3String(canonicalJson);
    }

    @Override
    public String revisionHash(Collection<T> items, Function<T, UUID> idFn) {
        Objects.requireNonNull(items, "items cannot be null");
        Objects.requireNonNull(idFn, "idFn cannot be null");

        var lines =
                items.stream()
                        .map(
                                it -> {
                                    var id =
                                            Objects.requireNonNull(
                                                    idFn.apply(it), "idFn returned null");
                                    var h = itemHash(it);
                                    return id + ":" + h;
                                })
                        .sorted(Comparator.naturalOrder())
                        .collect(Collectors.joining("\n"));

        return EncryptionUtil.sha3String(lines);
    }
}
