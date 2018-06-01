package io.pillopl.eventsourcing.application;

import io.pillopl.eventsourcing.model.CreditCard;
import io.pillopl.eventsourcing.model.Withdrawal;
import io.pillopl.eventsourcing.persistence.CreditCardRepository;
import io.pillopl.eventsourcing.persistence.WithdrawalRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WithdrawalsProcess {

    private final CreditCardRepository creditCardRepository;
    private final WithdrawalRepository withdrawalRepository;

    public WithdrawalsProcess(CreditCardRepository creditCardRepository, WithdrawalRepository withdrawalRepository) {
        this.creditCardRepository = creditCardRepository;
        this.withdrawalRepository = withdrawalRepository;
    }

    @Transactional
    public void withdraw(UUID cardId, BigDecimal amount) {
        CreditCard creditCard = creditCardRepository.load(cardId);
        creditCard.withdraw(amount);
        withdrawalRepository.save(new Withdrawal(UUID.randomUUID(), cardId, amount));
    }
}
