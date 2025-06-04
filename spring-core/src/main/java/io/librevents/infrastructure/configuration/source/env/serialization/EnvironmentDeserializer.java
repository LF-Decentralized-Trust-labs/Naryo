package io.librevents.infrastructure.configuration.source.env.serialization;

import com.fasterxml.jackson.databind.JsonDeserializer;

public abstract class EnvironmentDeserializer<T> extends JsonDeserializer<T> {}
