package io.naryo.infrastructure.configuration.persistence.document.filter;

import io.naryo.domain.filter.FilterType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "filters")
@Getter
public abstract class FilterDocument {

    @MongoId
    private String id;

    @NotNull
    private String name;

    @NotNull
    private FilterType type;

    @NotNull
    private String nodeId;

}
