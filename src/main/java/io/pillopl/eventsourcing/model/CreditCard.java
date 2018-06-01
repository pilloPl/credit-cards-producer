package io.pillopl.eventsourcing.model;

import io.vavr.Predicates;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.collection.List.ofAll;

public class CreditCard {

    private final UUID uuid;
    private BigDecimal limit;
    private BigDecimal usedLimit = BigDecimal.ZERO;
    private int withdrawals;
    private List<DomainEvent> pendingEvents = new ArrayList<>();

    public CreditCard(UUID uuid) {
        this.uuid = uuid;
    }

    public void assignLimit(BigDecimal amount) { //cmd
        if(limitWasAlreadyAssigned()) { //invariant
            throw new IllegalStateException(); //nack
        }
        //ack
        limitAssigned(new LimitAssigned(uuid, amount, Instant.now()));

    }

    private CreditCard limitAssigned(LimitAssigned event) {
        this.limit = event.getAmount();
        pendingEvents.add(event);
        return this;
    }

    private boolean limitWasAlreadyAssigned() {
        return limit != null;
    }

    public void withdraw(BigDecimal amount) {
        if(notEnoughMoneyToWIthdraw(amount)) {
            throw new IllegalStateException();
        }

        if(tooManyWithdrawalsInCycle()) {
            throw new IllegalStateException();
        }
        cardWithdrawn(new CardWithdrawn(uuid, amount, Instant.now()));

    }

    private CreditCard cardWithdrawn(CardWithdrawn event) {
        this.usedLimit = usedLimit.add(event.getAmount());
        this.withdrawals++;
        pendingEvents.add(event);
        return this;
    }

    private boolean tooManyWithdrawalsInCycle() {
        return withdrawals >= 45;
    }

    private boolean notEnoughMoneyToWIthdraw(BigDecimal amount) {
        return availableLimit().compareTo(amount) < 0;
    }

    void repay(BigDecimal amount) {
        cardRepaid(new CardRepaid(uuid, amount, Instant.now()));
    }

    private CreditCard cardRepaid(CardRepaid event) {
        usedLimit = usedLimit.subtract(event.getAmount());
        pendingEvents.add(event);
        return this;
    }

    void billingCycleClosed() {
        cycleClosed(new CycleClosed(uuid, Instant.now()));
    }

    private CreditCard cycleClosed(CycleClosed event) {
        withdrawals = 0;
        pendingEvents.add(event);
        return this;
    }


    public BigDecimal availableLimit() {
        return limit.subtract(usedLimit);
    }

    public UUID getUuid() {
        return uuid;
    }

    public List<DomainEvent> getPendingEvents() {
        return pendingEvents;
    }

    public void eventsFlushed() {
        pendingEvents.clear();
    }

    public static CreditCard recreateFrom(UUID uuid, List<DomainEvent> events) {
        return ofAll(events).foldLeft(new CreditCard(uuid), CreditCard::handle);
    }

    private CreditCard handle(DomainEvent event) {
        return io.vavr.API.Match(event).of(
                Case($(Predicates.instanceOf(LimitAssigned.class)), this::limitAssigned),
                Case($(Predicates.instanceOf(CardWithdrawn.class)), this::cardWithdrawn),
                Case($(Predicates.instanceOf(CardRepaid.class)), this::cardRepaid),
                Case($(Predicates.instanceOf(CycleClosed.class)), this::cycleClosed)
        );
    }
}
