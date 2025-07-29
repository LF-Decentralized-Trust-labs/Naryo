package io.naryo.application.configuration.source.model;

import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class DescriptorTestUuidArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        UUID original = UUID.randomUUID();
        UUID other = UUID.randomUUID();

        return Stream.of(
                Arguments.of(original, other, original),
                Arguments.of(original, null, original),
                Arguments.of(null, other, other),
                Arguments.of(null, null, null));
    }
}
