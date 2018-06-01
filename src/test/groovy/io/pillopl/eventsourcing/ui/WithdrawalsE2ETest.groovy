package io.pillopl.eventsourcing.ui

import io.pillopl.eventsourcing.model.CreditCard
import io.pillopl.eventsourcing.model.Withdrawal
import io.pillopl.eventsourcing.persistence.CreditCardRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WithdrawalsE2ETest extends Specification {

    @Autowired TestRestTemplate template
    @Autowired CreditCardRepository cardRepository
    UUID uuid = UUID.randomUUID()

    def setup() {
        CreditCard card = new CreditCard(uuid)
        card.assignLimit(1000.00)
        cardRepository.save(card)
    }

    def 'should save and load card'() {
        given:
            template.postForEntity("/withdrawals", new WithdrawalRequest(uuid, 10.00), Void.class)
        when:
            ResponseEntity<List<Withdrawal>> withdrawals =
                    template.exchange("/withdrawals/{uuid}", HttpMethod.GET, null, new ParameterizedTypeReference<List<Withdrawal>>() {}, ["uuid": uuid])
        then:
            withdrawals.statusCode.is2xxSuccessful()
            withdrawals.getBody().size() == 1
    }


}
