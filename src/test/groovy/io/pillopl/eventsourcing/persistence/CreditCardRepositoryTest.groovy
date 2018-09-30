package io.pillopl.eventsourcing.persistence

import io.pillopl.eventsourcing.model.CreditCard
import org.springframework.cloud.stream.messaging.Source
import spock.lang.Specification

class CreditCardRepositoryTest extends Specification {

    Source source = Stub()
    CreditCardRepository creditCardRepository = new CreditCardRepository(source)

    def 'should save and load card'() {
        given:
            UUID uuid = UUID.randomUUID()
        and:
            CreditCard card = new CreditCard(uuid)
        and:
            card.assignLimit(100)
        and:
            card.withdraw(10)
        when:
            creditCardRepository.save(card)
        and:
            CreditCard loaded = creditCardRepository.load(uuid)
        then:
            loaded.availableLimit() == 90
            loaded.getPendingEvents().size() == 0
    }
}
