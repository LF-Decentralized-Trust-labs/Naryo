package io.naryo.infrastructure.configuration.persistence.entity.node.subscription;

import java.util.UUID;

import io.naryo.application.configuration.source.model.node.subscription.SubscriptionDescriptor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subscriptions")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "subscription_type", discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor
public abstract class SubscriptionEntity implements SubscriptionDescriptor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    @Getter
    private UUID id;
}
