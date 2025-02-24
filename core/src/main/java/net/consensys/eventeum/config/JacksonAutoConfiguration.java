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

package net.consensys.eventeum.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.consensys.eventeum.dto.event.parameter.NumberParameter;
import net.consensys.eventeum.dto.event.serializer.NumberParameterSerializer;
import net.consensys.eventeum.integration.mixin.PageMixIn;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;

/**
 * Configures the jackson ObjectMapper bean.
 *
 * @author Craig Williams <craig.williams@consensys.net>
 */
@AutoConfiguration
@ConditionalOnClass(ObjectMapper.class)
public class JacksonAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(ObjectMapper.class)
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.addMixIn(Page.class, PageMixIn.class);

    SimpleModule module = new SimpleModule();
    module.addSerializer(NumberParameter.class, new NumberParameterSerializer());
    mapper.registerModule(module);

    return mapper;
  }
}
