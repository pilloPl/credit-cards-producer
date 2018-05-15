package io.pillopl.eventsourcing;

import io.pillopl.eventsourcing.model.CreditCard;
import io.pillopl.eventsourcing.persistence.CreditCardRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootApplication
@EnableBinding(Source.class)
@EnableScheduling
public class EventsourcingApplication {

    private final CreditCardRepository creditCardRepository;

    public EventsourcingApplication(CreditCardRepository creditCardRepository) {
        this.creditCardRepository = creditCardRepository;
    }

    public static void main(String[] args) {
		SpringApplication.run(EventsourcingApplication.class, args);
	}

	@Scheduled(fixedRate = 2000)
    public void randomCard() {
        CreditCard creditCard = new CreditCard(UUID.randomUUID());
        creditCard.assignLimit(BigDecimal.TEN);
        creditCardRepository.save(creditCard);
    }

}
