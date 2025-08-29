package com.example.testnaryo;

import java.math.BigInteger;
import java.time.Duration;
import java.util.*;

import io.naryo.application.node.NodeContainer;
import io.naryo.application.node.NodeInitializer;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.infrastructure.configuration.persistence.document.broadcaster.configuration.BroadcasterCacheConfiguration;
import io.naryo.infrastructure.configuration.persistence.document.broadcaster.configuration.BroadcasterConfigurationDocument;
import io.naryo.infrastructure.configuration.persistence.document.broadcaster.target.BlockBroadcasterTargetDocument;
import io.naryo.infrastructure.configuration.persistence.document.broadcaster.target.BroadcasterDocument;
import io.naryo.infrastructure.configuration.persistence.entity.broadcaster.BroadcasterEntity;
import io.naryo.infrastructure.configuration.persistence.entity.broadcaster.configuration.BroadcasterCacheEntity;
import io.naryo.infrastructure.configuration.persistence.entity.broadcaster.configuration.BroadcasterConfigurationEntity;
import io.naryo.infrastructure.configuration.persistence.entity.broadcaster.target.BlockBroadcasterTargetEntity;
import io.naryo.infrastructure.configuration.persistence.entity.common.ConnectionEndpointEntity;
import io.naryo.infrastructure.configuration.persistence.entity.filter.event.EventSpecification;
import io.naryo.infrastructure.configuration.persistence.entity.filter.event.FilterVisibility;
import io.naryo.infrastructure.configuration.persistence.entity.filter.event.GlobalEventFilterEntity;
import io.naryo.infrastructure.configuration.persistence.entity.filter.event.sync.BlockFilterSyncEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.connection.http.HttpConnectionEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.hedera.HederaNodeEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.interaction.block.HederaMirrorNodeBlockInteractionEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.subscription.block.PubSubBlockSubscriptionEntity;
import io.naryo.infrastructure.configuration.persistence.repository.broadcaster.BroadcasterConfigurationDocumentRepository;
import io.naryo.infrastructure.configuration.persistence.repository.broadcaster.BroadcasterConfigurationEntityRepository;
import io.naryo.infrastructure.configuration.persistence.repository.broadcaster.BroadcasterDocumentRepository;
import io.naryo.infrastructure.configuration.persistence.repository.broadcaster.BroadcasterEntityRepository;
import io.naryo.infrastructure.configuration.persistence.repository.filter.FilterEntityRepository;
import io.naryo.infrastructure.configuration.persistence.repository.node.NodeEntityRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.example.testnaryo", "io.naryo"})
public class Application implements InitializingBean {

    private final NodeInitializer nodeInitializer;

    private final BroadcasterConfigurationEntityRepository jpaConfigRepository;
    private final BroadcasterEntityRepository jpaBroadcasterRepository;
    private final FilterEntityRepository jpaFilterEntityRepository;
    private final NodeEntityRepository jpaNodeEntityRepository;

    private final BroadcasterConfigurationDocumentRepository
            broadcasterConfigurationDocumentRepository;
    private final BroadcasterDocumentRepository broadcasterDocumentRepository;

    public Application(
            NodeInitializer nodeInitializer,
            BroadcasterConfigurationEntityRepository jpaConfigRepository,
            BroadcasterEntityRepository jpaBroadcasterRepository,
            BroadcasterConfigurationDocumentRepository broadcasterConfigurationDocumentRepository,
            BroadcasterDocumentRepository broadcasterDocumentRepository,
            FilterEntityRepository jpafilterEntityRepository,
            NodeEntityRepository jpaNodeEntityRepository) {
        this.nodeInitializer = nodeInitializer;

        this.jpaConfigRepository = jpaConfigRepository;
        this.jpaBroadcasterRepository = jpaBroadcasterRepository;
        this.jpaFilterEntityRepository = jpafilterEntityRepository;

        this.broadcasterConfigurationDocumentRepository =
                broadcasterConfigurationDocumentRepository;
        this.broadcasterDocumentRepository = broadcasterDocumentRepository;
        this.jpaNodeEntityRepository = jpaNodeEntityRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        jpaFilterEntityRepository
                .findAll()
                .forEach(
                        filter -> {
                            if (filter instanceof GlobalEventFilterEntity globalFilter) {
                                // Limpiar la colección de estados
                                globalFilter.setStatuses(Collections.emptySet());
                                jpaFilterEntityRepository.saveAndFlush(globalFilter);
                            }
                        });

        // Esperar un momento para asegurar que las operaciones anteriores se completen
        Thread.sleep(100);

        // Luego eliminar todo en el orden correcto
        jpaFilterEntityRepository.deleteAllInBatch();
        jpaConfigRepository.deleteAllInBatch();
        jpaBroadcasterRepository.deleteAllInBatch();
        jpaNodeEntityRepository.deleteAllInBatch();

        broadcasterConfigurationDocumentRepository.deleteAll();
        broadcasterDocumentRepository.deleteAll();

        // Comprobar si los repositorios están vacíos
        if (jpaConfigRepository.count() == 0
                && jpaBroadcasterRepository.count() == 0
                && broadcasterConfigurationDocumentRepository.count() == 0
                && broadcasterDocumentRepository.count() == 0) {

            // Crear configuración
            UUID configId = UUID.randomUUID();
            BroadcasterConfigurationEntity jpaConfig =
                    new BroadcasterConfigurationEntity(
                            configId,
                            "http",
                            new BroadcasterCacheEntity(Duration.ofMinutes(5)),
                            Map.of("endpoint", Map.of("url", "http://localhost:8080")),
                            null);

            // Crear broadcaster
            UUID broadcasterId = UUID.randomUUID();
            BroadcasterEntity jpaBroadcaster =
                    new BroadcasterEntity(
                            broadcasterId, configId, new BlockBroadcasterTargetEntity("/events"));

            // Guardar en JPA
            jpaConfigRepository.save(jpaConfig);
            jpaBroadcasterRepository.save(jpaBroadcaster);

            // Crear documentos MongoDB
            BroadcasterConfigurationDocument mongoConfig =
                    new BroadcasterConfigurationDocument(
                            configId.toString(),
                            "http",
                            new BroadcasterCacheConfiguration(Duration.ofMinutes(5)),
                            Map.of("endpoint", Map.of("url", "http://localhost:8080")),
                            null);

            BroadcasterDocument mongoBroadcaster =
                    new BroadcasterDocument(
                            broadcasterId.toString(),
                            configId.toString(),
                            new BlockBroadcasterTargetDocument("/events"));

            // Guardar en MongoDB
            broadcasterConfigurationDocumentRepository.save(mongoConfig);
            broadcasterDocumentRepository.save(mongoBroadcaster);
        }

        if (jpaFilterEntityRepository.count() == 0) {
            // Crear la especificación del evento
            EventSpecification specification =
                    new EventSpecification(
                            "Transfer(address indexed, address indexed, uint256)", 69);

            // Configurar todos los estados posibles del evento
            Set<ContractEventStatus> statuses = EnumSet.allOf(ContractEventStatus.class);

            // Configurar la sincronización
            BlockFilterSyncEntity sync = new BlockFilterSyncEntity(BigInteger.ZERO);

            // Configurar la visibilidad
            FilterVisibility visibility = new FilterVisibility(true, null);

            // Crear el filtro global
            GlobalEventFilterEntity globalFilter =
                    new GlobalEventFilterEntity(
                            UUID.fromString("fa1abf87-fc96-44a1-ae20-c4aecb3d5909"),
                            "my-transfer-filter-from-jpa",
                            UUID.fromString("061d75e5-2016-4e98-bdfa-d6cd32b28529"),
                            specification,
                            statuses,
                            sync,
                            visibility);

            // Guardar el filtro en la base de datos
            jpaFilterEntityRepository.save(globalFilter);
        }

        if (jpaNodeEntityRepository.count() == 0) {

            HttpConnectionEntity connection =
                    new HttpConnectionEntity(
                            null,
                            new ConnectionEndpointEntity("http:yoquse.com"),
                            null,
                            null,
                            null,
                            null);

            var node1 =
                    new HederaNodeEntity(
                            UUID.randomUUID(),
                            "Hedera Node from JPA",
                            new PubSubBlockSubscriptionEntity(null, null, null, null, null, null),
                            new HederaMirrorNodeBlockInteractionEntity(),
                            connection);

            this.jpaNodeEntityRepository.save(node1);
        }

        NodeContainer container = nodeInitializer.init();
        container.start();
    }
}
