package io.naryo.infrastructure.hook;

import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.diff.DiffResult;
import io.naryo.application.configuration.revision.hook.RevisionHook;
import io.naryo.domain.filter.Filter;
import io.naryo.infrastructure.configuration.persistence.entity.filter.FilterEntity;
import io.naryo.infrastructure.configuration.persistence.repository.filter.FilterEntityRepository;
import org.springframework.stereotype.Component;

@Component
public class FilterRevisionHook implements RevisionHook<Filter> {

    private final FilterEntityRepository repository;

    public FilterRevisionHook(FilterEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onBeforeApply(DiffResult<Filter> diff) {
        for (Filter add : diff.added()) {
            addFilter(add);
        }
        for (Filter remove : diff.removed()) {
            removeFilter(remove);
        }
        for (DiffResult.Modified<Filter> modified : diff.modified()) {
            updateFilter(modified.after());
        }
    }

    @Override
    public void onAfterApply(Revision<Filter> applied, DiffResult<Filter> diff) {}

    private void addFilter(Filter filter) {
        repository.save(FilterEntity.fromDomain(filter));
    }

    private void removeFilter(Filter filter) {
        repository.deleteById(filter.getId());
    }

    private void updateFilter(Filter filter) {
        repository
                .findById(filter.getId())
                .ifPresent(
                        filterDocument -> {
                            repository.save(FilterEntity.fromDomain(filter));
                        });
    }
}
