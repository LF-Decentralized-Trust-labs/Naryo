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

import io.naryo.endpoint.response.MonitorTransactionsResponse;
import io.naryo.model.TransactionMonitoringSpec;
import io.naryo.service.TransactionMonitoringService;
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
@RequestMapping(value = "/api/rest/v1/transaction")
@AllArgsConstructor
public class TransactionMonitoringEndpoint {

    private TransactionMonitoringService monitoringService;

    /**
     * Monitors a transaction with the specified hash, on a specific node
     *
     * @param spec the transaction spec to add
     * @param response the http response
     */
    @PostMapping
    public MonitorTransactionsResponse monitorTransactions(
            @RequestBody TransactionMonitoringSpec spec, HttpServletResponse response) {
        spec.generateId();
        spec.convertToCheckSum();
        monitoringService.registerTransactionsToMonitor(spec);
        response.setStatus(HttpServletResponse.SC_ACCEPTED);

        return new MonitorTransactionsResponse(spec.getId());
    }

    /**
     * Returns the list of registered {@link MonitorTransactionsResponse}
     *
     * @param response the http response
     */
    @GetMapping
    public List<TransactionMonitoringSpec> getMonitorTransactions(HttpServletResponse response) {
        List<TransactionMonitoringSpec> registeredTransactions =
                monitoringService.listTransactionMonitorings();
        response.setStatus(HttpServletResponse.SC_ACCEPTED);

        return registeredTransactions;
    }

    /**
     * Stops monitoring a transaction with the specfied hash
     *
     * @param id the id of the transaction monitor to remove
     * @param nodeName the name of the node where the transaction is being monitored
     * @param response the http response
     */
    @DeleteMapping(value = "/{id}")
    public void stopMonitoringTransaction(
            @PathVariable String id,
            @RequestParam(required = false) String nodeName,
            HttpServletResponse response) {

        try {
            monitoringService.stopMonitoringTransactions(id);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (NotFoundException e) {
            // Rethrow endpoint exception with response information
            throw new TransactionNotFoundEndpointException();
        }
    }
}
