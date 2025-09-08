package io.naryo.infrastructure.store.event.persistence.entity.block;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.event.block.BlockEvent;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "block_event", indexes = @Index(name = "ux_number", columnList = "number DESC"))
@Getter
@NoArgsConstructor
public final class BlockEventEntity {

    private @Id @GeneratedValue(strategy = GenerationType.UUID) @Column(name = "id") UUID id;

    private @Column(name = "node_id", nullable = false) String nodeId;

    private @Column(name = "number", nullable = false) BigInteger number;

    private @Column(name = "hash", nullable = false, unique = true) String hash;

    private @Column(name = "logs_bloom", nullable = false, length = 1024) String logsBloom;

    private @Column(name = "size", nullable = false) BigInteger size;

    private @Column(name = "gas_used", nullable = false) BigInteger gasUsed;

    private @Column(name = "timestamp", nullable = false) BigInteger timestamp;

    private @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "block_id") List<BlockEventTransactionEntity> transactions;

    private BlockEventEntity(
            String nodeId,
            BigInteger number,
            String hash,
            String logsBloom,
            BigInteger size,
            BigInteger gasUsed,
            BigInteger timestamp,
            List<BlockEventTransactionEntity> transactions) {
        this.nodeId = nodeId;
        this.number = number;
        this.hash = hash;
        this.logsBloom = logsBloom;
        this.size = size;
        this.gasUsed = gasUsed;
        this.timestamp = timestamp;
        this.transactions = transactions;
    }

    public static BlockEventEntity fromBlockEvent(BlockEvent blockEvent) {
        return new BlockEventEntity(
                blockEvent.getNodeId().toString(),
                blockEvent.getNumber().value(),
                blockEvent.getHash(),
                blockEvent.getLogsBloom(),
                blockEvent.getSize(),
                blockEvent.getGasUsed(),
                blockEvent.getTimestamp(),
                blockEvent.getTransactions().stream()
                        .map(BlockEventTransactionEntity::fromTransaction)
                        .collect(Collectors.toList()));
    }

    public BlockEvent toBlockEvent() {
        return new BlockEvent(
                UUID.fromString(nodeId),
                new NonNegativeBlockNumber(number),
                hash,
                logsBloom,
                size,
                gasUsed,
                timestamp,
                transactions.stream().map(BlockEventTransactionEntity::toTransaction).toList());
    }
}
