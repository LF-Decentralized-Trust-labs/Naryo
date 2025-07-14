package io.naryo.infrastructure.configuration.provider.filter.mapper.event;

import io.naryo.domain.filter.event.ParameterDefinition;
import io.naryo.domain.filter.event.parameter.*;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition.*;

import java.util.stream.Collectors;

public abstract class ParameterDefinitionDocumentMapper {

    public static ParameterDefinition fromDocument(ParameterDefinitionPropertiesDocument props) {
        switch (props) {
            case AddressParameterDefinitionPropertiesDocument addressProps -> {
                return new AddressParameterDefinition(addressProps.getPosition(), addressProps.isIndexed());
            }
            case ArrayParameterDefinitionPropertiesDocument arrayProps -> {
                return new ArrayParameterDefinition(
                    arrayProps.getPosition(),
                    ParameterDefinitionDocumentMapper.fromDocument(arrayProps.getElementType()),
                    arrayProps.getFixedLength()
                );
            }
            case BoolParameterDefinitionPropertiesDocument boolProps -> {
                return new BoolParameterDefinition(boolProps.getPosition(), boolProps.isIndexed());
            }
            case BytesFixedParameterDefinitionPropertiesDocument bytesProps -> {
                return new BytesFixedParameterDefinition(
                    bytesProps.getByteLength(), bytesProps.getPosition(), bytesProps.isIndexed());
            }
            case BytesParameterDefinitionPropertiesDocument ignored -> {
                return new BytesParameterDefinition(props.getPosition());
            }
            case IntParameterDefinitionPropertiesDocument intProps -> {
                return new IntParameterDefinition(
                    intProps.getBitSize(), intProps.getPosition(), intProps.isIndexed());
            }
            case StringParameterDefinitionPropertiesDocument ignored -> {
                return new StringParameterDefinition(props.getPosition());
            }
            case StructParameterDefinitionPropertiesDocument structProps -> {
                return new StructParameterDefinition(
                    structProps.getPosition(),
                    structProps.getParameterDefinitions()
                        .stream()
                        .map(ParameterDefinitionDocumentMapper::fromDocument)
                        .collect(Collectors.toSet())
                );
            }
            case UintParameterDefinitionPropertiesDocument uintProps -> {
                return new UintParameterDefinition(
                    uintProps.getBitSize(), uintProps.getPosition(), uintProps.isIndexed());
            }
            default ->
                throw new IllegalStateException("Unsupported parameter type: " + props.getClass());
        }
    }
}
