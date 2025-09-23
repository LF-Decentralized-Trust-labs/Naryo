package io.naryo.application.configuration.normalization;

import io.naryo.domain.configuration.Configuration;
import io.naryo.domain.normalization.Normalizer;

public interface ConfigurationNormalizerRegistry<T extends Configuration> {

    <S> void register(String type, Class<S> sourceClass, Normalizer<T> normalizer);

    <S> T normalize(String type, S source);
}
