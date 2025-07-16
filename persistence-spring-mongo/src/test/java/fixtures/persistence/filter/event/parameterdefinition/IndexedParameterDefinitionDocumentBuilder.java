package fixtures.persistence.filter.event.parameterdefinition;

import io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition.IndexedParameterDefinitionDocument;
import org.instancio.Instancio;

public abstract class IndexedParameterDefinitionDocumentBuilder<T, Y extends IndexedParameterDefinitionDocument>
    extends ParameterDefinitionDocumentBuilder<T, Y> {

    private Boolean indexed;

    public T withIndexed(boolean indexed) {
        this.indexed = indexed;
        return this.self();
    }

    protected boolean isIndexed() {
        return this.indexed == null
            ? Instancio.create(Boolean.class)
            : this.indexed;
    }
}
