package io.naryo.infrastructure.configuration.persistence.document.node.interaction;

import io.naryo.application.configuration.source.model.node.interaction.InteractionDescriptor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public abstract class InteractionPropertiesDocument implements InteractionDescriptor {}
