package io.naryo.infrastructure.configuration.persistence.entity.broadcaster.configuration.schema;

import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class ConfigurationSchemaEntity {

    private @Column(name = "schema_type") String type;

    private @ElementCollection(fetch = FetchType.EAGER) @CollectionTable(
            name = "broadcasters_configuration_fields",
            joinColumns = @JoinColumn(name = "broadcaster_configuration_id")) List<
                    FieldDefinitionEntity>
            fields;

    public ConfigurationSchemaEntity(String type, List<FieldDefinitionEntity> fields) {
        this.type = type;
        this.fields = fields;
    }
}
