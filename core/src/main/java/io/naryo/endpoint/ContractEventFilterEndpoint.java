/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.naryo.endpoint;

import java.util.List;

import io.naryo.dto.event.filter.ContractEventFilter;
import io.naryo.endpoint.response.AddEventFilterResponse;
import io.naryo.service.SubscriptionService;
import io.naryo.service.exception.NotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * A REST endpoint for adding a removing event filters.
 *
 * @author Craig Williams craig.williams@consensys.net
 */
@RestController
@RequestMapping(value = "/api/rest/v1/event-filter")
@AllArgsConstructor
public class ContractEventFilterEndpoint {

    private SubscriptionService filterService;

    /**
     * Adds an event filter with the specification described in the ContractEventFilter.
     *
     * @param eventFilter the event filter to add
     * @param response the http response
     */
    @PostMapping
    public AddEventFilterResponse addEventFilter(
            @RequestBody ContractEventFilter eventFilter, HttpServletResponse response) {

        final ContractEventFilter registeredFilter =
                filterService.registerContractEventFilter(eventFilter, true);
        response.setStatus(HttpServletResponse.SC_ACCEPTED);

        return new AddEventFilterResponse(registeredFilter.getId());
    }

    /**
     * Returns the list of registered {@link ContractEventFilter}
     *
     * @param response the http response
     */
    @GetMapping
    public List<ContractEventFilter> listEventFilters(HttpServletResponse response) {
        List<ContractEventFilter> registeredFilters = filterService.listContractEventFilters();
        response.setStatus(HttpServletResponse.SC_OK);

        return registeredFilters;
    }

    /**
     * Deletes an event filter with the corresponding filter id.
     *
     * @param filterId the filterId to delete
     * @param response the http response
     */
    @DeleteMapping(value = "/{filterId}")
    public void removeEventFilter(@PathVariable String filterId, HttpServletResponse response) {

        try {
            filterService.unregisterContractEventFilter(filterId, true);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (NotFoundException e) {
            // Rethrow endpoint exception with response information
            throw new FilterNotFoundEndpointException();
        }
    }
}
