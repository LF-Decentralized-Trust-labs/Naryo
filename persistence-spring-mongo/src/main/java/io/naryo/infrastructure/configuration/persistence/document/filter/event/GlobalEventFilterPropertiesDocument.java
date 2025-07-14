package io.naryo.infrastructure.configuration.persistence.document.filter.event;

import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("global_event_filter")
@Getter
public class GlobalEventFilterPropertiesDocument extends EventFilterPropertiesDocument {
}
