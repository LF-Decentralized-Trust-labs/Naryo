package io.naryo.application.configuration.source.model;

import java.util.stream.Stream;

import org.instancio.Instancio;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class DescriptorTestStringArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        String original = Instancio.create(String.class);
        String other = Instancio.create(String.class);

        return Stream.of(
                Arguments.of(original, other, original),
                Arguments.of(original, null, original),
                Arguments.of(null, other, other),
                Arguments.of(null, null, null));
    }
}
