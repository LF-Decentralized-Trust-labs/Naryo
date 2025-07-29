package io.naryo.application.configuration.source.model;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class DescriptorTestBooleanArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return Stream.of(
                Arguments.of(true, false, true),
                Arguments.of(true, null, true),
                Arguments.of(null, false, false),
                Arguments.of(null, null, null));
    }
}
