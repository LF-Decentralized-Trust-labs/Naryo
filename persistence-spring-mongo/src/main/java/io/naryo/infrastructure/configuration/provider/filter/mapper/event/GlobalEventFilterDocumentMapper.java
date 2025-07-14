package io.naryo.infrastructure.configuration.provider.filter.mapper.event;

import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.event.GlobalEventFilter;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.GlobalEventFilterPropertiesDocument;
import io.naryo.infrastructure.configuration.provider.filter.mapper.event.syncstate.SyncStateDocumentMapper;

import java.util.UUID;

public abstract class GlobalEventFilterDocumentMapper {

    public static GlobalEventFilter fromDocument(GlobalEventFilterPropertiesDocument props) {
        return new GlobalEventFilter(
            UUID.fromString(props.getId()),
            new FilterName(props.getName()),
            UUID.fromString(props.getNodeId()),
            EventFilterSpecificationDocumentMapper.fromDocument(props.getSpecification()),
            props.getStatuses(),
            SyncStateDocumentMapper.fromDocument(props.getSyncState()),
            EventFilterVisibilityConfigurationDocumentMapper.fromDocument(props.getVisibilityConfiguration())
        );
    }
}
