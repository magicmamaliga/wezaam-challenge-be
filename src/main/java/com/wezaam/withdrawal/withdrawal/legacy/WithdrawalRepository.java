package com.wezaam.withdrawal.withdrawal.legacy;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {
}
