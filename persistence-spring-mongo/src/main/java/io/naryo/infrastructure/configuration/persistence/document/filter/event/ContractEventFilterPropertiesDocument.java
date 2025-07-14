package io.naryo.infrastructure.configuration.persistence.document.filter.event;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("contract_event_filter")
@Getter
public class ContractEventFilterPropertiesDocument extends EventFilterPropertiesDocument {
    @NotNull
    private String contractAddress;

}
