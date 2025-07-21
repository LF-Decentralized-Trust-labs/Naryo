package io.naryo.infrastructure.configuration.persistence.document.broadcaster.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FieldDefinitionDocument {
    private String name;
    private String typeName;
    private boolean required;
    private Object defaultValue;
}
