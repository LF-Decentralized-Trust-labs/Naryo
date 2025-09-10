package io.naryo.infrastructure.store.filter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import io.naryo.application.store.filter.FilterStore;
import io.naryo.application.store.filter.model.FilterState;
import io.naryo.domain.configuration.store.active.JpaActiveStoreConfiguration;
import io.naryo.infrastructure.store.JpaStore;
import io.naryo.infrastructure.store.filter.persistence.entity.FilterStateEntity;
import io.naryo.infrastructure.store.filter.persistence.repository.FilterStateEntityRepository;
import org.springframework.stereotype.Component;

@Component
public class JpaFilterStateStore extends JpaStore<UUID, FilterState>
        implements FilterStore<JpaActiveStoreConfiguration> {

    private final FilterStateEntityRepository filterStateEntityRepository;

    public JpaFilterStateStore(FilterStateEntityRepository filterStateEntityRepository) {
        this.filterStateEntityRepository = filterStateEntityRepository;
    }

    @Override
    protected Class<?> getTargetDataClass() {
        return FilterState.class;
    }

    @Override
    public void save(JpaActiveStoreConfiguration configuration, UUID key, FilterState data) {
        filterStateEntityRepository.save(FilterStateEntity.fromFilterState(data));
    }

    @Override
    public Optional<FilterState> get(JpaActiveStoreConfiguration configuration, UUID key) {
        return filterStateEntityRepository.findById(key).map(FilterStateEntity::toFilterState);
    }

    @Override
    public List<FilterState> get(JpaActiveStoreConfiguration configuration, List<UUID> keys) {
        return filterStateEntityRepository.findAllByFilterIdIn(keys).stream()
                .map(FilterStateEntity::toFilterState)
                .collect(Collectors.toList());
    }
}
