package io.naryo.infrastructure.configuration.provider.filter.mapper.event;

import io.naryo.domain.filter.event.ParameterDefinition;
import io.naryo.domain.filter.event.parameter.*;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition.*;

import java.util.stream.Collectors;

public abstract class ParameterDefinitionDocumentMapper {

    public static ParameterDefinition fromDocument(ParameterDefinitionDocument props) {
        switch (props) {
            case AddressParameterDefinitionDocument addressProps -> {
                return new AddressParameterDefinition(addressProps.getPosition(), addressProps.isIndexed());
            }
            case ArrayParameterDefinitionDocument arrayProps -> {
                return new ArrayParameterDefinition(
                    arrayProps.getPosition(),
                    ParameterDefinitionDocumentMapper.fromDocument(arrayProps.getElementType()),
                    arrayProps.getFixedLength()
                );
            }
            case BoolParameterDefinitionDocument boolProps -> {
                return new BoolParameterDefinition(boolProps.getPosition(), boolProps.isIndexed());
            }
            case BytesFixedParameterDefinitionDocument bytesProps -> {
                return new BytesFixedParameterDefinition(
                    bytesProps.getByteLength(), bytesProps.getPosition(), bytesProps.isIndexed());
            }
            case BytesParameterDefinitionDocument ignored -> {
                return new BytesParameterDefinition(props.getPosition());
            }
            case IntParameterDefinitionDocument intProps -> {
                return new IntParameterDefinition(
                    intProps.getBitSize(), intProps.getPosition(), intProps.isIndexed());
            }
            case StringParameterDefinitionDocument ignored -> {
                return new StringParameterDefinition(props.getPosition());
            }
            case StructParameterDefinitionDocument structProps -> {
                return new StructParameterDefinition(
                    structProps.getPosition(),
                    structProps.getParameterDefinitions()
                        .stream()
                        .map(ParameterDefinitionDocumentMapper::fromDocument)
                        .collect(Collectors.toSet())
                );
            }
            case UintParameterDefinitionDocument uintProps -> {
                return new UintParameterDefinition(
                    uintProps.getBitSize(), uintProps.getPosition(), uintProps.isIndexed());
            }
            default ->
                throw new IllegalStateException("Unsupported parameter type: " + props.getClass());
        }
    }
}
