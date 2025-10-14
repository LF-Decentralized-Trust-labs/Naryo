package io.naryo.api.filter.controller;

import java.util.List;

import io.naryo.api.error.ConfigurationApiErrors;
import io.naryo.api.filter.response.FilterResponse;
import io.naryo.application.filter.revision.FilterConfigurationRevisionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
public class GetFiltersController extends FilterController {

    private final FilterConfigurationRevisionManager filterConfigurationRevisionManager;

    @GetMapping
    @ConfigurationApiErrors
    @ResponseStatus(HttpStatus.OK)
    public List<FilterResponse> getAll() {
        var liveView = filterConfigurationRevisionManager.liveView();
        var fingerprints = liveView.itemFingerprintById();
        return liveView.revision().domainItems().stream()
                .map(f -> FilterResponse.map(f, fingerprints))
                .toList();
    }
}
