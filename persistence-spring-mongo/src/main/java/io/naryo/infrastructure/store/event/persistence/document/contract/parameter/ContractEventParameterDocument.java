package io.naryo.infrastructure.store.event.persistence.document.contract.parameter;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.ContractEventParameter;
import io.naryo.domain.event.contract.parameter.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor
@Document
public abstract class ContractEventParameterDocument<V, T extends ContractEventParameter<?>> {

    private ParameterType type;

    private boolean indexed;

    private int position;

    private V value;

    protected ContractEventParameterDocument(
            ParameterType type, boolean indexed, int position, V value) {
        this.type = type;
        this.indexed = indexed;
        this.position = position;
        this.value = value;
    }

    public abstract T toDomain();

    public static ContractEventParameterDocument<?, ?> fromDomain(
            ContractEventParameter<?> eventParameter) {
        return switch (eventParameter) {
            case AddressParameter addressParameter ->
                    AddressParameterDocument.fromAddressParameter(addressParameter);
            case ArrayParameter<?> arrayParameter ->
                    ArrayParameterDocument.fromArrayParameter(arrayParameter);
            case BoolParameter boolParameter -> BoolParameterDocument.from(boolParameter);
            case BytesFixedParameter bytesFixedParameter ->
                    BytesFixedParameterDocument.from(bytesFixedParameter);
            case BytesParameter bytesParameter -> BytesParameterDocument.from(bytesParameter);
            case IntParameter intParameter -> IntParameterDocument.from(intParameter);
            case StringParameter stringParameter -> StringParameterDocument.from(stringParameter);
            case StructParameter structParameter -> StructParameterDocument.from(structParameter);
            case UintParameter uintParameter -> UintParameterDocument.from(uintParameter);
            default ->
                    throw new IllegalStateException(
                            "Unexpected eventParameter: " + eventParameter.getClass());
        };
    }
}
