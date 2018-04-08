package io.pillopl.eventsourcing.model;

import java.time.Instant;
import java.util.UUID;

public class CycleClosed implements DomainEvent {

    private final UUID cardNo;
    private final Instant timestamp;

    public CycleClosed(UUID cardNo, Instant timestamp) {
        this.cardNo = cardNo;
        this.timestamp = timestamp;
    }

    public UUID getCardNo() {
        return cardNo;
    }

    public Instant getTimestamp() {
        return timestamp;
    }


    @Override
    public String getType() {
        return "cycle-closed";
    }
}
