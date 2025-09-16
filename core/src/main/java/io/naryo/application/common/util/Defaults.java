package io.naryo.application.common.util;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Defaults {

    public static <T> void setDefault(
            Supplier<Optional<T>> getter, Consumer<T> setter, T defaultValue) {
        setter.accept(getter.get().orElse(defaultValue));
    }
}
