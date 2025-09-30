package io.naryo.infrastructure.hook;

import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.diff.DiffResult;
import io.naryo.application.configuration.revision.hook.RevisionHook;
import io.naryo.application.configuration.source.model.filter.FilterDescriptor;
import io.naryo.infrastructure.configuration.persistence.entity.filter.FilterEntity;
import io.naryo.infrastructure.configuration.persistence.repository.filter.FilterEntityRepository;
import org.springframework.stereotype.Component;

@Component
public class FilterRevisionHook implements RevisionHook<FilterDescriptor> {

    private final FilterEntityRepository repository;

    public FilterRevisionHook(FilterEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onBeforeApply(DiffResult<FilterDescriptor> diff) {
        for (FilterDescriptor add : diff.added()) {
            addFilter(add);
        }
        for (FilterDescriptor remove : diff.removed()) {
            removeFilter(remove);
        }
        for (DiffResult.Modified<FilterDescriptor> modified : diff.modified()) {
            updateFilter(modified.after());
        }
    }

    @Override
    public void onAfterApply(
            Revision<FilterDescriptor> applied, DiffResult<FilterDescriptor> diff) {}

    private void addFilter(FilterDescriptor descriptor) {
        repository.save(FilterEntity.fromDescriptor(descriptor));
    }

    private void removeFilter(FilterDescriptor descriptor) {
        repository.deleteById(descriptor.getId());
    }

    private void updateFilter(FilterDescriptor descriptor) {
        repository.findById(descriptor.getId()).ifPresent(repository::save);
    }
}
