package fixtures.persistence.filter.event.parameterdefinition;

import io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition.ParameterDefinitionDocument;
import org.instancio.Instancio;

public abstract class ParameterDefinitionDocumentBuilder<T, Y extends ParameterDefinitionDocument> {

    private Integer position;

    public abstract T self();

    public abstract Y build();

    public T withPosition(int position) {
        this.position = position;
        return this.self();
    }

    protected int getPosition() {
        return this.position == null
            ? Instancio.create(Integer.class)
            : this.position;
    }
}
