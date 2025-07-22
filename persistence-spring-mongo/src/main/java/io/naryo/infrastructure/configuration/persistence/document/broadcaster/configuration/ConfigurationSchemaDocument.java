package io.naryo.infrastructure.configuration.persistence.document.broadcaster.configuration;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@Document
public class ConfigurationSchemaDocument {
    private String type;
    private List<FieldDefinitionDocument> fields;
}
