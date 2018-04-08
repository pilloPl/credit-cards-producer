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
    private final Map<UUID, List<DomainEvent>> eventStream = new HashMap<>();

    public CreditCardRepository(Source source) {
        this.source = source;
    }


    public void save(CreditCard creditCard) {
        List<DomainEvent> currentStream = eventStream.getOrDefault(creditCard.getUuid(), new ArrayList<>());
        currentStream.addAll(creditCard.getPendingEvents());
        eventStream.put(creditCard.getUuid(), currentStream);
        creditCard.getPendingEvents().forEach(event -> source.output().send(new GenericMessage<>(event)));
        creditCard.flushEvents();
    }

    public CreditCard load(UUID uuid) {
        return CreditCard.recreateFrom(uuid, eventStream.getOrDefault(uuid, new ArrayList<>()));
    }
}
