package io.pillopl.eventsourcing.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class LimitAssigned implements DomainEvent {

    private final UUID cardNo;
    private final BigDecimal amount;
    private final Instant timestamp;

    public LimitAssigned(UUID cardNo, BigDecimal amount, Instant timestamp) {
        this.cardNo = cardNo;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public UUID getCardNo() {
        return cardNo;
    }

    @Override
    public String getType() {
        return "limit-assigned";
    }
}
