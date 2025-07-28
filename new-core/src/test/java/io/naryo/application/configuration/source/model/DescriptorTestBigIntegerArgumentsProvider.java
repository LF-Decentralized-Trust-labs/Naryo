package io.naryo.application.configuration.source.model;

import java.math.BigInteger;
import java.util.stream.Stream;

import org.instancio.Instancio;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class DescriptorTestBigIntegerArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        BigInteger original = Instancio.create(BigInteger.class);
        BigInteger other = Instancio.create(BigInteger.class);

        return Stream.of(
                Arguments.of(original, other, original),
                Arguments.of(original, null, original),
                Arguments.of(null, other, other),
                Arguments.of(null, null, null));
    }
}
