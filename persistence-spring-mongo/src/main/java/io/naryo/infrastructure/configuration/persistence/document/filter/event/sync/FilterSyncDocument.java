package io.naryo.infrastructure.configuration.persistence.document.filter.event.sync;

import io.naryo.application.configuration.source.model.filter.event.sync.FilterSyncDescriptor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public abstract class FilterSyncDocument implements FilterSyncDescriptor {

}
