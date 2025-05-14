package io.librevents.infrastructure.configuration.source.env.serialization;

import com.fasterxml.jackson.databind.JsonDeserializer;

public abstract class EnvironmentSerializer<T> extends JsonDeserializer<T> {}
