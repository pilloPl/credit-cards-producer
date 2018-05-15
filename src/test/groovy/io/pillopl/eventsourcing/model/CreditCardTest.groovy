package io.pillopl.eventsourcing.model

import spock.lang.Specification

class CreditCardTest extends Specification {

    CreditCard card = new CreditCard(UUID.randomUUID())

    def 'cannot reassign limit'() {
        given:
            card.assignLimit(100)
        when:
            card.assignLimit(50)
        then:
            thrown(IllegalStateException)

    }

    def 'can assign limit'() {
        when:
            card.assignLimit(50)
        then:
            card.availableLimit() == 50
    }

    def 'can withdraw'() {
        given:
            card.assignLimit(100)
        when:
            card.withdraw(50)
        then:
            card.availableLimit() == 50
    }

    def 'cannot withdraw when not enough money'() {
        given:
            card.assignLimit(100)
        when:
            card.withdraw(101)
        then:
            thrown(IllegalStateException)
    }

    def 'cannot withdraw when too many withdrawals in cycle'() {
        given:
            card.assignLimit(100)
        and:
            45.times { card.withdraw(1) }
        when:
            card.withdraw(1)
        then:
            thrown(IllegalStateException)
    }

    def 'can repay'() {
        given:
            card.assignLimit(100)
        and:
            card.withdraw(10)
        when:
            card.repay(5)
        then:
            card.availableLimit() == 95
    }

    def 'can withdraw in next cycle'() {
        given:
            card.assignLimit(100)
        and:
            45.times { card.withdraw(1) }
        and:
            card.billingCycleClosed()
        when:
            card.withdraw(1)
        then:
            card.availableLimit() == 54
    }
}

