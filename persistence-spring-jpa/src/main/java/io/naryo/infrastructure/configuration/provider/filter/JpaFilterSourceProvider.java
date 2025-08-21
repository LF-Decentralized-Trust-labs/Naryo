package io.naryo.infrastructure.configuration.provider.filter;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import io.naryo.application.configuration.source.model.filter.FilterDescriptor;
import io.naryo.application.filter.configuration.provider.FilterSourceProvider;
import io.naryo.infrastructure.configuration.persistence.entity.filter.FilterEntity;
import io.naryo.infrastructure.configuration.persistence.repository.filter.FilterEntityRepository;
import org.springframework.stereotype.Component;

@Component
public class JpaFilterSourceProvider implements FilterSourceProvider {

    private final FilterEntityRepository repository;

    public JpaFilterSourceProvider(FilterEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public Collection<FilterDescriptor> load() {
        List<FilterEntity> filters = this.repository.findAll();
        return new HashSet<>(filters);
    }

    @Override
    public int priority() {
        return 0;
    }
}
