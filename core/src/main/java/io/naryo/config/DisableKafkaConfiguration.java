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

package io.naryo.config;

import io.naryo.annotation.ConditionalOnKafkaRequired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * Disable Spring Boot Kafka auto configuration if its not needed.
 *
 * @author Craig Williams craig.williams@consensys.net
 */
@Configuration
@ConditionalOnKafkaRequired(false)
@EnableAutoConfiguration(
        exclude = {org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration.class})
public class DisableKafkaConfiguration {}
