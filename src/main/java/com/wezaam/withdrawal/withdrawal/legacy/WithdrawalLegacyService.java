package com.wezaam.withdrawal.withdrawal.legacy;

import com.wezaam.withdrawal.withdrawal.WithdrawalDTO;
import com.wezaam.withdrawal.withdrawal.WithdrawalFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class WithdrawalLegacyService {

    private final WithdrawalRepository withdrawalRepository;

    public WithdrawalLegacyService(WithdrawalRepository withdrawalRepository) {
        this.withdrawalRepository = withdrawalRepository;
    }

    public List<WithdrawalDTO> findAll(){
        log.info("Entering findAll");
        List<Withdrawal> withdrawals = withdrawalRepository.findAll();
        return WithdrawalFactory.createWithdrawalDTOsFromWithdrawals(withdrawals);
    }

}
