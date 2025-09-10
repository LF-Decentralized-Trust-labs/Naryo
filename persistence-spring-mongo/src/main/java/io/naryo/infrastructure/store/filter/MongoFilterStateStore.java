package io.naryo.infrastructure.store.filter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import io.naryo.application.store.filter.FilterStore;
import io.naryo.application.store.filter.model.FilterState;
import io.naryo.domain.configuration.store.MongoStoreConfiguration;
import io.naryo.infrastructure.store.MongoStore;
import io.naryo.infrastructure.store.filter.persistence.document.FilterStateDocument;
import io.naryo.infrastructure.store.filter.persistence.repository.FilterStateDocumentRepository;
import org.springframework.stereotype.Component;

@Component
public class MongoFilterStateStore extends MongoStore<UUID, FilterState>
        implements FilterStore<MongoStoreConfiguration> {

    private final FilterStateDocumentRepository filterStateEntityRepository;

    public MongoFilterStateStore(FilterStateDocumentRepository filterStateEntityRepository) {
        this.filterStateEntityRepository = filterStateEntityRepository;
    }

    @Override
    protected Class<?> getTargetDataClass() {
        return FilterState.class;
    }

    @Override
    public void save(MongoStoreConfiguration configuration, UUID key, FilterState data) {
        filterStateEntityRepository.save(FilterStateDocument.fromFilterState(data));
    }

    @Override
    public Optional<FilterState> get(MongoStoreConfiguration configuration, UUID key) {
        String id = key.toString();

        return filterStateEntityRepository.findById(id).map(FilterStateDocument::toFilterState);
    }

    @Override
    public List<FilterState> get(MongoStoreConfiguration configuration, List<UUID> keys) {
        List<String> ids = keys.stream().map(UUID::toString).toList();

        return filterStateEntityRepository.findAllByFilterIdIn(ids).stream()
                .map(FilterStateDocument::toFilterState)
                .collect(Collectors.toList());
    }
}
