package io.naryo.application.common.util;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import io.naryo.application.configuration.source.model.MergeableDescriptor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MergeUtil {

    public static <V> void mergeOptionals(
            Consumer<V> setter, Optional<V> original, Optional<V> other) {
        boolean objectsAreMergeable =
                original.isPresent()
                        && other.isPresent()
                        && !Objects.equals(original.get(), other.get());

        if (objectsAreMergeable) {
            setter.accept(other.get());
        } else other.ifPresent(setter);
    }

    public static <V extends Collection<?>> void mergeCollections(
            Consumer<V> setter, V original, V other) {
        if (!other.isEmpty() && !Objects.equals(original, other)) {
            setter.accept(other);
        }
    }

    public static <V extends MergeableDescriptor<V>> void mergeDescriptors(
            Consumer<V> setter, Optional<V> original, Optional<V> other) {
        boolean objectsAreMergeable =
                original.isPresent()
                        && other.isPresent()
                        && !Objects.equals(original.get(), other.get());

        if (objectsAreMergeable) {
            V merged = original.get().merge(other.get());
            setter.accept(merged);
        } else other.ifPresent(setter);
    }
}
