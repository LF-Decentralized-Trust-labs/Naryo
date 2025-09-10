package io.naryo.infrastructure.store.filter.persistence.repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import io.naryo.infrastructure.store.filter.persistence.entity.FilterStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilterStateEntityRepository extends JpaRepository<FilterStateEntity, UUID> {

    List<FilterStateEntity> findAllByFilterIdIn(Collection<UUID> filterId);
}
