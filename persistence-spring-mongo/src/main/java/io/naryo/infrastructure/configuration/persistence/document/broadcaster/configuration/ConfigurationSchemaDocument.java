package io.naryo.infrastructure.configuration.persistence.document.broadcaster.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class ConfigurationSchemaDocument {
    private String type;
    private List<FieldDefinitionDocument> fields;

}
