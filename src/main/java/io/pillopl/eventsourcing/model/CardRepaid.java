package io.pillopl.eventsourcing.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class CardRepaid implements DomainEvent {

    private final UUID cardNo;
    private final BigDecimal amount;
    private final Instant timestamp;

    public CardRepaid(UUID cardNo, BigDecimal amount, Instant timestamp) {
        this.cardNo = cardNo;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public UUID getCardNo() {
        return cardNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public String getType() {
        return "card-repaid";
    }
}
