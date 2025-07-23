package fixtures.persistence.filter;

import java.util.UUID;

import io.naryo.infrastructure.configuration.persistence.document.filter.FilterDocument;
import org.instancio.Instancio;

public abstract class FilterDocumentBuilder<T, Y extends FilterDocument> {

    private String id;
    private String name;
    private String nodeId;

    public abstract T self();

    public abstract Y build();

    public T withId(UUID id) {
        this.id = id.toString();
        return self();
    }

    public T withName(String name) {
        this.name = name;
        return self();
    }

    public T withNodeId(String nodeId) {
        this.nodeId = nodeId;
        return self();
    }

    protected String getId() {
        return this.id == null ? UUID.randomUUID().toString() : this.id;
    }

    protected String getName() {
        return this.name == null ? Instancio.create(String.class) : this.name;
    }

    protected String getNodeId() {
        return this.nodeId == null ? UUID.randomUUID().toString() : this.nodeId;
    }
}
