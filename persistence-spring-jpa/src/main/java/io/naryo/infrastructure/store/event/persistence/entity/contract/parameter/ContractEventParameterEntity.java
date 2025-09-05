package io.naryo.infrastructure.store.event.persistence.entity.contract.parameter;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.OptBoolean;
import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.ContractEventParameter;
import io.naryo.domain.event.contract.parameter.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        requireTypeIdForSubtypes = OptBoolean.TRUE)
@JsonSubTypes({
    @JsonSubTypes.Type(value = AddressParameterEntity.class, name = "ADDRESS"),
    @JsonSubTypes.Type(value = ArrayParameterEntity.class, name = "ARRAY"),
    @JsonSubTypes.Type(value = BoolParameterEntity.class, name = "BOOL"),
    @JsonSubTypes.Type(value = BytesFixedParameterEntity.class, name = "BYTES_FIXED"),
    @JsonSubTypes.Type(value = BytesParameterEntity.class, name = "BYTES"),
    @JsonSubTypes.Type(value = IntParameterEntity.class, name = "INT"),
    @JsonSubTypes.Type(value = StringParameterEntity.class, name = "STRING"),
    @JsonSubTypes.Type(value = StructParameterEntity.class, name = "STRUCT"),
    @JsonSubTypes.Type(value = UintParameterEntity.class, name = "UINT")
})
public abstract class ContractEventParameterEntity<V> {

    private ParameterType type;

    private boolean indexed;

    private int position;

    private V value;

    protected ContractEventParameterEntity(
            ParameterType type, boolean indexed, int position, V value) {
        this.type = type;
        this.indexed = indexed;
        this.position = position;
        this.value = value;
    }

    public static ContractEventParameterEntity<?> from(ContractEventParameter<?> eventParameter) {
        return switch (eventParameter) {
            case AddressParameter addressParameter -> AddressParameterEntity.from(addressParameter);
            case ArrayParameter<?> arrayParameter -> ArrayParameterEntity.from(arrayParameter);
            case BoolParameter boolParameter -> BoolParameterEntity.from(boolParameter);
            case BytesFixedParameter bytesFixedParameter ->
                BytesFixedParameterEntity.from(bytesFixedParameter);
            case BytesParameter bytesParameter -> BytesParameterEntity.from(bytesParameter);
            case IntParameter intParameter -> IntParameterEntity.from(intParameter);
            case StringParameter stringParameter -> StringParameterEntity.from(stringParameter);
            case StructParameter structParameter -> StructParameterEntity.from(structParameter);
            case UintParameter uintParameter -> UintParameterEntity.from(uintParameter);
            default -> throw new IllegalStateException(
                "Unexpected eventParameter: " + eventParameter.getClass());
        };
    }
}
