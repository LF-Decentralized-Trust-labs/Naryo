package io.naryo.infrastructure.configuration.persistence.entity.common.schema;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FieldDefinitionEntity {

    private @Column(name = "name") String name;

    private @Column(name = "type_name") String typeName;

    private @Column(name = "required") boolean required;

    private @Column(name = "default_value") @JdbcTypeCode(SqlTypes.JSON) Object defaultValue;
}
