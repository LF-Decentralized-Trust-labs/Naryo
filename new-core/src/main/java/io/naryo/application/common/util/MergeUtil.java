package io.naryo.application.common.util;

import io.naryo.application.configuration.source.model.MergeableDescriptor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class MergeUtil {

    public static <V> void mergeOptionals(Consumer<Optional<V>> setter,
                                          Optional<V> original,
                                          Optional<V> other) {
        if (original.isPresent() &&
            other.isPresent() &&
            !Objects.equals(original.get(), other.get())) {
            setter.accept(other);
        } else if (other.isPresent()) {
            setter.accept(other);
        }
    }

    public static <V> void mergeLists(Consumer<List<V>> setter,
                                      List<V> originalList,
                                      List<V> otherList) {
        setter.accept(
            Stream.concat(originalList.stream(), otherList.stream()).toList()
        );
    }

    public static <V extends MergeableDescriptor<V>> void mergeDescriptors(Consumer<Optional<V>> setter,
                                                                           Optional<V> original,
                                                                           Optional<V> other) {
        boolean objectsAreMergeable = original.isPresent() &&
            other.isPresent() &&
            !Objects.equals(original.get(), other.get());

        if (objectsAreMergeable) {
            V merged = original.get().merge(other.get());
            setter.accept(Optional.of(merged));
        } else if (other.isPresent()) {
            setter.accept(other);
        }
    }

}
