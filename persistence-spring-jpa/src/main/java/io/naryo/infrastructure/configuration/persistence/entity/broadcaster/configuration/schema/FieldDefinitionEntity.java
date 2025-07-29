package io.naryo.infrastructure.configuration.persistence.entity.broadcaster.configuration.schema;

import io.naryo.infrastructure.configuration.persistence.entity.broadcaster.util.JsonObjectConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FieldDefinitionEntity {

    private String name;

    private String typeName;

    private boolean required;

    @Column(name = "default_value", columnDefinition = "TEXT")
    @Convert(converter = JsonObjectConverter.class)
    private Object defaultValue;
}
