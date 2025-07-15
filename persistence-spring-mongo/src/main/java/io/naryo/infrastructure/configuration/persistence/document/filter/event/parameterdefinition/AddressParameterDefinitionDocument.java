package io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition;

import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("address_parameter_definition")
@Getter
public class AddressParameterDefinitionDocument extends IndexedParameterDefinitionDocument {

}
