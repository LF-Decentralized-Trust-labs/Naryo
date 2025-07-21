package io.naryo.infrastructure.configuration.provider.filter;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import io.naryo.application.configuration.source.model.filter.FilterDescriptor;
import io.naryo.application.filter.configuration.provider.FilterSourceProvider;
import io.naryo.infrastructure.configuration.persistence.document.filter.FilterDocument;
import io.naryo.infrastructure.configuration.persistence.repository.filter.FilterDocumentRepository;
import org.springframework.stereotype.Component;

@Component
public final class MongoFilterSourceProvider implements FilterSourceProvider {

    private final FilterDocumentRepository repository;

    public MongoFilterSourceProvider(FilterDocumentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Collection<FilterDescriptor> load() {
        List<FilterDocument> filters = this.repository.findAll();
        return new HashSet<>(filters);
    }

    @Override
    public int priority() {
        return 0;
    }
}
