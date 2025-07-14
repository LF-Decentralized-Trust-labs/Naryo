package io.naryo.infrastructure.configuration.provider.filter;

import io.naryo.application.filter.configuration.provider.FilterConfigurationProvider;
import io.naryo.domain.filter.Filter;
import io.naryo.infrastructure.configuration.persistence.document.filter.FilterPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.repository.filter.FilterDocumentRepository;
import io.naryo.infrastructure.configuration.provider.filter.mapper.FilterDocumentMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public final class MongoFilterConfigurationProvider implements FilterConfigurationProvider {

    private final FilterDocumentRepository repository;

    public MongoFilterConfigurationProvider(FilterDocumentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Collection<Filter> load() {
        List<FilterPropertiesDocument> filters = this.repository.findAll();

        Set<Filter> filterDocuments = filters.stream()
            .map(FilterDocumentMapper::fromDocument)
            .collect(Collectors.toSet());

        return filterDocuments;
    }

    @Override
    public int priority() {
        return 1;
    }

}
