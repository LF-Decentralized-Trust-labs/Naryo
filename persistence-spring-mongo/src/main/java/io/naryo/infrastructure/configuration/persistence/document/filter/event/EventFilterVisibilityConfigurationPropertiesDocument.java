package io.naryo.infrastructure.configuration.persistence.document.filter.event;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class EventFilterVisibilityConfigurationPropertiesDocument {

    @NotNull
    private boolean visible;

    @NotNull
    private String privacyGroupId;

}
