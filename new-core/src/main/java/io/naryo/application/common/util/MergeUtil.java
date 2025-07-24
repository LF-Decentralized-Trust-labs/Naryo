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
        if (original.isEmpty() && other.isPresent()) {
            setter.accept(other.get());
        }
    }

    public static <V extends Collection<?>> void mergeCollections(
            Consumer<V> setter, V original, V other) {
        if (original.isEmpty() && !other.isEmpty()) {
            setter.accept(other);
        }
    }

    public static <V extends MergeableDescriptor<V>> void mergeDescriptors(
            Consumer<V> setter, Optional<V> original, Optional<V> other) {
        boolean originalIsPresent = original.isPresent();
        boolean otherIsPresent = other.isPresent();

        if (!originalIsPresent && otherIsPresent) {
            setter.accept(other.get());
        } else if (originalIsPresent
                && otherIsPresent
                && !Objects.equals(original.get(), other.get())) {
            setter.accept(original.get().merge(other.get()));
        }
    }
}
