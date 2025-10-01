package io.naryo.infrastructure.hook;

import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.diff.DiffResult;
import io.naryo.application.configuration.revision.hook.RevisionHook;
import io.naryo.domain.filter.Filter;
import io.naryo.infrastructure.configuration.persistence.document.filter.FilterDocument;
import io.naryo.infrastructure.configuration.persistence.repository.filter.FilterDocumentRepository;
import org.springframework.stereotype.Component;

@Component
public class FilterDocumentRevisionHook implements RevisionHook<Filter> {

    private final FilterDocumentRepository repository;

    public FilterDocumentRevisionHook(FilterDocumentRepository repository) {
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

    private void addFilter(Filter descriptor) {
        repository.save(FilterDocument.fromDomain(descriptor));
    }

    private void removeFilter(Filter descriptor) {
        repository.deleteById(descriptor.getId().toString());
    }

    private void updateFilter(Filter descriptor) {
        repository.findById(descriptor.getId().toString()).ifPresent(repository::save);
    }
}
