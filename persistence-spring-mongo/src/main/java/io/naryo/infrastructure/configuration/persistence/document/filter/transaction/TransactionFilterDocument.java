package io.naryo.infrastructure.configuration.persistence.document.filter.transaction;

import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.filter.transaction.IdentifierType;
import io.naryo.infrastructure.configuration.persistence.document.filter.FilterDocument;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

import java.util.List;

@TypeAlias("transaction_filter")
@Getter
public class TransactionFilterDocument extends FilterDocument {

    @NotNull
    private IdentifierType identifierType;

    @NotNull
    private String value;

    @NotNull
    private List<TransactionStatus> statuses;

}
