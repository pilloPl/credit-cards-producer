package io.pillopl.eventsourcing.persistence;

import io.pillopl.eventsourcing.model.Withdrawal;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface WithdrawalRepository extends CrudRepository<Withdrawal, UUID> {

    List<Withdrawal> findByCardId(UUID cardId);
}
