package io.pillopl.eventsourcing.ui;

import io.pillopl.eventsourcing.application.WithdrawalsProcess;
import io.pillopl.eventsourcing.model.Withdrawal;
import io.pillopl.eventsourcing.persistence.WithdrawalRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
public class WithdrawalsController {

    private final WithdrawalRepository withdrawalRepository;
    private final WithdrawalsProcess withdrawalsProcess;

    public WithdrawalsController(WithdrawalRepository withdrawalRepository, WithdrawalsProcess withdrawalsProcess) {
        this.withdrawalRepository = withdrawalRepository;
        this.withdrawalsProcess = withdrawalsProcess;
    }

    @PostMapping("/withdrawals")
    ResponseEntity withdraw(@RequestBody  WithdrawalRequest withdrawalRequest) {
        withdrawalsProcess.withdraw(withdrawalRequest.getCardId(), withdrawalRequest.getAmount());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/withdrawals/{cardId}")
    ResponseEntity<List<Withdrawal>> withdrawals(@PathVariable UUID cardId) {
        return ResponseEntity.ok().body(withdrawalRepository.findByCardId(cardId));
    }
}

class WithdrawalRequest {
    private UUID cardId;
    private BigDecimal amount;

    WithdrawalRequest() {

    }

    WithdrawalRequest(UUID cardId, BigDecimal amount) {
        this.cardId = cardId;
        this.amount = amount;
    }

    public UUID getCardId() {
        return cardId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}