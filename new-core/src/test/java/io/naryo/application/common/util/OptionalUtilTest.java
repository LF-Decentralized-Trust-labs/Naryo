package io.naryo.application.common.util;

import java.util.Optional;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class OptionalUtilTest {

    @Test
    void testValueOrNullWithMapper_emptyOptional() {
        String input = "test";
        Function<String, Optional<String>> mapper = s -> Optional.empty();

        String result = OptionalUtil.valueOrNull(mapper, input);

        assertNull(result, "Result should be null when mapper returns an empty Optional");
    }

    @Test
    void testValueOrNullWithMapper_nonEmptyOptional() {
        String input = "test";
        Function<String, Optional<String>> mapper = s -> Optional.of(input);

        String result = OptionalUtil.valueOrNull(mapper, input);

        assertEquals(
                input,
                result,
                "Should return the value when the mapper function returns a non-empty Optional");
    }

    @Test
    void testValueOrNull_emptyOptional() {
        Optional<String> emptyOptional = Optional.empty();

        String result = OptionalUtil.valueOrNull(emptyOptional);

        assertNull(result, "Should return null when given an empty Optional");
    }

    @Test
    void testValueOrNull_nonEmptyOptional() {
        String expectedValue = "test";
        Optional<String> nonEmptyOptional = Optional.of(expectedValue);

        String result = OptionalUtil.valueOrNull(nonEmptyOptional);

        assertEquals(
                expectedValue, result, "Should return the value when given a non-empty Optional");
    }
}
