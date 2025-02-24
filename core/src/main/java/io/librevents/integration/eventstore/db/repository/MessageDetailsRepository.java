package io.librevents.integration.eventstore.db.repository;

import java.util.Optional;

import io.librevents.dto.message.MessageDetails;
import io.librevents.factory.EventStoreFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("messageDetailsRepository")
@ConditionalOnProperty(name = "eventStore.type", havingValue = "DB")
@ConditionalOnMissingBean(EventStoreFactory.class)
public interface MessageDetailsRepository extends CrudRepository<MessageDetails, String> {

    Optional<MessageDetails> findFirstByNodeNameAndTopicId(
            String nodeName, String topicId, Sort sort);
}
