package fixtures.persistence.filter.transaction;

import fixtures.persistence.filter.FilterDocumentBuilder;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.filter.transaction.IdentifierType;
import io.naryo.infrastructure.configuration.persistence.document.filter.transaction.TransactionFilterDocument;
import org.instancio.Instancio;

import java.util.Set;

public class TransactionFilterDocumentBuilder
    extends FilterDocumentBuilder<TransactionFilterDocumentBuilder, TransactionFilterDocument> {

    private IdentifierType identifierType;
    private String value;
    private Set<TransactionStatus> statuses;

    @Override
    public TransactionFilterDocumentBuilder self() {
        return this;
    }

    public TransactionFilterDocument build() {
        return new TransactionFilterDocument(
            this.getId(),
            this.getName(),
            this.getNodeId(),
            this.getIdentifierType(),
            this.getValue(),
            this.getStatuses()
        );
    }

    public TransactionFilterDocumentBuilder withIdentifierType(IdentifierType identifierType) {
        this.identifierType = identifierType;
        return self();
    }

    public TransactionFilterDocumentBuilder withStatuses(Set<TransactionStatus> statuses) {
        this.statuses = statuses;
        return self();
    }

    public TransactionFilterDocumentBuilder withValue(String value) {
        this.value = value;
        return self();
    }

    private IdentifierType getIdentifierType() {
        return this.identifierType == null
            ? Instancio.create(IdentifierType.class)
            : this.identifierType;
    }

    private Set<TransactionStatus> getStatuses() {
        return this.statuses == null
            ? Instancio.createSet(TransactionStatus.class)
            : this.statuses;
    }

    private String getValue() {
        return this.value == null
            ? Instancio.create(String.class)
            : this.value;
    }
}
