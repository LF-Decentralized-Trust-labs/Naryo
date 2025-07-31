package io.naryo.application.configuration.source.model;

import java.time.Duration;
import java.util.stream.Stream;

import org.instancio.Instancio;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class DescriptorTestDurationArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        Duration original = Instancio.create(Duration.class);
        Duration other = Instancio.create(Duration.class);

        return Stream.of(
                Arguments.of(original, other, original),
                Arguments.of(original, null, original),
                Arguments.of(null, other, other),
                Arguments.of(null, null, null));
    }
}
