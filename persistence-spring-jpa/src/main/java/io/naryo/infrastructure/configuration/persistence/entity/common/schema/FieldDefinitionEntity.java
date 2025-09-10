package io.naryo.infrastructure.configuration.persistence.entity.common.schema;

import io.naryo.infrastructure.configuration.persistence.entity.common.converter.JsonObjectConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FieldDefinitionEntity {

    private @Column(name = "name") String name;

    private @Column(name = "type_name") String typeName;

    private @Column(name = "required") boolean required;

    @Column(name = "default_value", columnDefinition = "TEXT")
    @Convert(converter = JsonObjectConverter.class)
    private Object defaultValue;
}
