package io.librevents.dto.event.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import io.librevents.dto.event.parameter.NumberParameter;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public class NumberParameterSerializer extends JsonSerializer<NumberParameter> {
    @Override
    public void serialize(
            NumberParameter value,
            JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeStringField("type", value.getType());
        jsonGenerator.writeStringField("value", value.getValueString());
    }

    @Override
    public void serializeWithType(
            NumberParameter value,
            JsonGenerator gen,
            SerializerProvider provider,
            TypeSerializer typeSer)
            throws IOException {

        typeSer.writeTypePrefixForObject(value, gen);
        serialize(value, gen, provider);
        typeSer.writeTypeSuffixForObject(value, gen);
    }
}
