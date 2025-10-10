package io.naryo.api.filter;

import java.util.List;
import java.util.UUID;

import io.naryo.api.error.ConfigurationApiErrors;
import io.naryo.api.filter.mapper.FilterRequestMapper;
import io.naryo.api.filter.mapper.FilterResponseMapper;
import io.naryo.api.filter.request.FilterRequest;
import io.naryo.api.filter.response.FilterResponse;
import io.naryo.application.configuration.revision.OperationId;
import io.naryo.application.configuration.revision.RevisionOperationStatus;
import io.naryo.application.configuration.revision.operation.AddOperation;
import io.naryo.application.configuration.revision.operation.RemoveOperation;
import io.naryo.application.configuration.revision.operation.RevisionOperation;
import io.naryo.application.configuration.revision.operation.UpdateOperation;
import io.naryo.application.configuration.revision.queue.RevisionOperationQueue;
import io.naryo.application.configuration.revision.store.RevisionOperationStore;
import io.naryo.application.filter.revision.FilterConfigurationRevisionManager;
import io.naryo.domain.filter.Filter;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/filters")
@RequiredArgsConstructor
public class FilterController {

    private final @Qualifier("filterRevisionQueue") RevisionOperationQueue<Filter> operationQueue;
    private final FilterConfigurationRevisionManager filterConfigurationRevisionManager;
    private final RevisionOperationStore operationStore;

    @GetMapping
    @ConfigurationApiErrors
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<FilterResponse> getAll() {
        return filterConfigurationRevisionManager.liveRegistry().active().domainItems().stream()
                .map(FilterResponseMapper::map)
                .toList();
    }

    @PostMapping
    @ConfigurationApiErrors
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RevisionOperationStatus create(@Valid @RequestBody FilterRequest filterRequest) {
        Filter filter = FilterRequestMapper.toDomain(filterRequest);
        RevisionOperation<Filter> op = new AddOperation<>(filter);
        OperationId opId = operationQueue.enqueue(op);
        return operationStore
                .get(opId.value())
                .orElseThrow(
                        () ->
                                new ValidationException(
                                        "Operation status not found: " + opId.value()));
    }

    @PutMapping("/{id}")
    @ConfigurationApiErrors
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RevisionOperationStatus update(
            @PathVariable("id") UUID id,
            @RequestParam("prevItemHash") String prevItemHash,
            @Valid @RequestBody FilterRequest filterRequest) {
        Filter proposed = FilterRequestMapper.toDomain(filterRequest);
        RevisionOperation<Filter> op = new UpdateOperation<>(id, prevItemHash, proposed);
        OperationId opId = operationQueue.enqueue(op);
        return operationStore
                .get(opId.value())
                .orElseThrow(
                        () ->
                                new ValidationException(
                                        "Operation status not found: " + opId.value()));
    }

    @DeleteMapping("/{id}")
    @ConfigurationApiErrors
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RevisionOperationStatus delete(
            @PathVariable("id") UUID id, @RequestParam("prevItemHash") String prevItemHash) {
        RevisionOperation<Filter> op = new RemoveOperation<>(id, prevItemHash);
        var opId = operationQueue.enqueue(op);
        return operationStore
                .get(opId.value())
                .orElseThrow(
                        () ->
                                new ValidationException(
                                        "Operation status not found: " + opId.value()));
    }
}
