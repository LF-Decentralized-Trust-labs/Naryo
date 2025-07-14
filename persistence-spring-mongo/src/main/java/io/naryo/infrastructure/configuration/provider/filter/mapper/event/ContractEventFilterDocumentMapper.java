package io.naryo.infrastructure.configuration.provider.filter.mapper.event;

import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.event.ContractEventFilter;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.ContractEventFilterPropertiesDocument;
import io.naryo.infrastructure.configuration.provider.filter.mapper.event.syncstate.SyncStateDocumentMapper;

import java.util.UUID;

public abstract class ContractEventFilterDocumentMapper {

    public static ContractEventFilter fromDocument(ContractEventFilterPropertiesDocument props) {
        return new ContractEventFilter(
            UUID.fromString(props.getUuid()),
            new FilterName(props.getName()),
            UUID.fromString(props.getNodeId()),
            EventFilterSpecificationDocumentMapper.fromDocument(props.getSpecification()),
            props.getStatuses(),
            SyncStateDocumentMapper.fromDocument(props.getSyncState()),
            EventFilterVisibilityConfigurationDocumentMapper.fromDocument(props.getVisibilityConfiguration()),
            props.getContractAddress()
        );
    }
}
