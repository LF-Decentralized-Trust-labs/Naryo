package io.naryo.infrastructure.configuration.persistence.document.broadcaster.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@Document
public class FieldDefinitionDocument {
    private String name;
    private String typeName;
    private boolean required;
    private Object defaultValue;
}
