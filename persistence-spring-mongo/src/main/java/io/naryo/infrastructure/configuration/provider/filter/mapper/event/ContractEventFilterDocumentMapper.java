package io.naryo.infrastructure.configuration.provider.filter.mapper.event;

import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.event.ContractEventFilter;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.ContractEventFilterDocument;
import io.naryo.infrastructure.configuration.provider.filter.mapper.event.syncstate.SyncStateDocumentMapper;

import java.util.UUID;

public abstract class ContractEventFilterDocumentMapper {

    public static ContractEventFilter fromDocument(ContractEventFilterDocument props) {
        return new ContractEventFilter(
            UUID.fromString(props.getId()),
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
