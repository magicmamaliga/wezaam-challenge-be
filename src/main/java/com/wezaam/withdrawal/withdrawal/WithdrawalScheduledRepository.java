package com.wezaam.withdrawal.withdrawal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface WithdrawalScheduledRepository extends JpaRepository<WithdrawalScheduled, Long> {
    //If it worked I keep it like this, but would also add findALlByExecuteAtBeforeAndWithdrawalStatus
    // with Instant and PENDING
    List<WithdrawalScheduled> findAllByExecuteAtBefore(Instant date);

    List<WithdrawalScheduled> findAllByExecuteAtBeforeAndStatus(
            Instant executeAt,
            WithdrawalStatus status
    );
}
