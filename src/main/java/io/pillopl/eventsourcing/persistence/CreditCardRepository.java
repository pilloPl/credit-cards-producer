package io.pillopl.eventsourcing.persistence;

import io.pillopl.eventsourcing.model.CreditCard;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class CreditCardRepository {

    public void save(CreditCard creditCard) {

    }

    public CreditCard load(UUID uuid) {
        return null;
    }
}
