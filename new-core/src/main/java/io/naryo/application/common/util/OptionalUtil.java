package io.naryo.application.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OptionalUtil {

    public static <T, R> R valueOrNull(Function<T, Optional<R>> mapper, T source) {
        return mapper.apply(source).orElse(null);
    }

    public static <T> T valueOrNull(Optional<T> source) {
        return source.orElse(null);
    }

}
