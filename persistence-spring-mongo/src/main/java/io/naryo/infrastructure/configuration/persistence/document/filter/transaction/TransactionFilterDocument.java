package io.naryo.infrastructure.configuration.persistence.document.filter.transaction;

import io.naryo.application.configuration.source.model.filter.transaction.TransactionFilterDescriptor;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.filter.FilterType;
import io.naryo.domain.filter.transaction.IdentifierType;
import io.naryo.infrastructure.configuration.persistence.document.filter.FilterDocument;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

import java.util.List;

@TypeAlias("transaction_filter")
@Getter
@Setter
public class TransactionFilterDocument extends FilterDocument implements TransactionFilterDescriptor {

    @NotNull
    private IdentifierType identifierType;

    @NotNull
    private String value;

    @NotNull
    private List<TransactionStatus> statuses;

    public TransactionFilterDocument(String id,
                                     String name,
                                     String nodeId,
                                     IdentifierType identifierType,
                                     String value,
                                     List<TransactionStatus> statuses) {
        super(id, name, FilterType.TRANSACTION, nodeId);
        this.identifierType = identifierType;
        this.value = value;
        this.statuses = statuses;
    }

}
