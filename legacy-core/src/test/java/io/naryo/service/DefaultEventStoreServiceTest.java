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

package io.naryo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import io.naryo.dto.event.ContractEventDetails;
import io.naryo.integration.eventstore.EventStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultEventStoreServiceTest {

    private static final String EVENT_SIGNATURE = "signature";

    private static final String CONTRACT_ADDRESS = "0xd94a9d6733a64cecdcc8ca01da72554b4d883a47";

    private DefaultEventStoreService underTest;

    @Mock private EventStore mockEventStore;

    @Mock private ContractEventDetails mockEventDetails1;

    @Mock private ContractEventDetails mockEventDetails2;

    @Mock private Page<ContractEventDetails> mockPage;

    @BeforeEach
    public void init() {
        underTest = new DefaultEventStoreService(mockEventStore);
    }

    @Test
    void testGetLatestContractEvent() {
        when(mockPage.getContent()).thenReturn(Arrays.asList(mockEventDetails1, mockEventDetails2));
        when(mockEventStore.getContractEventsForSignature(
                        eq(EVENT_SIGNATURE), eq(CONTRACT_ADDRESS), any(PageRequest.class)))
                .thenReturn(mockPage);
        assertEquals(
                mockEventDetails1,
                underTest.getLatestContractEvent(EVENT_SIGNATURE, CONTRACT_ADDRESS).get());
    }

    @Test
    void testGetLatestContractEventNullEvents() {
        when(mockPage.getContent()).thenReturn(Collections.emptyList());
        when(mockEventStore.getContractEventsForSignature(
                        eq(EVENT_SIGNATURE), eq(CONTRACT_ADDRESS), any(PageRequest.class)))
                .thenReturn(mockPage);
        assertEquals(
                false,
                underTest.getLatestContractEvent(EVENT_SIGNATURE, CONTRACT_ADDRESS).isPresent());
    }

    @Test
    void testGetLatestContractEventEmptyEvents() {
        when(mockPage.getContent()).thenReturn(new ArrayList<>());
        when(mockEventStore.getContractEventsForSignature(
                        eq(EVENT_SIGNATURE), eq(CONTRACT_ADDRESS), any(PageRequest.class)))
                .thenReturn(mockPage);
        assertEquals(
                false,
                underTest.getLatestContractEvent(EVENT_SIGNATURE, CONTRACT_ADDRESS).isPresent());
    }
}
