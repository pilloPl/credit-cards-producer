package io.pillopl.eventsourcing.persistence;

import io.pillopl.eventsourcing.model.CreditCard;
import io.pillopl.eventsourcing.model.DomainEvent;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CreditCardRepository {

    private final Source source;
    private final Map<UUID, List<DomainEvent>> eventStreams = new HashMap<>();

    public CreditCardRepository(Source source) {
        this.source = source;
    }

    public void save(CreditCard creditCard) {
        List<DomainEvent> currentStream
                = eventStreams.getOrDefault(creditCard.getUuid(), new ArrayList<>());
        currentStream.addAll(creditCard.getPendingEvents());
        creditCard.getPendingEvents().forEach(event ->
        source.output().send(new GenericMessage<>(event)));
        eventStreams.put(creditCard.getUuid(), currentStream);
        creditCard.eventsFlushed();
    }

    public CreditCard load(UUID uuid) {
        return CreditCard.recreateFrom(uuid, eventStreams.getOrDefault(uuid, new ArrayList<>()));
    }
}
