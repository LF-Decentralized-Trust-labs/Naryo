package io.librevents.infrastructure.util.serialization;

import java.io.IOException;
import java.time.Duration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.boot.convert.DurationStyle;

public final class DurationDeserializer extends JsonDeserializer<Duration> {
    @Override
    public Duration deserialize(JsonParser p, DeserializationContext context) throws IOException {
        return DurationStyle.detectAndParse(p.getText().trim());
    }
}
