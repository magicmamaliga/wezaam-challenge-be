package com.wezaam.withdrawal.withdrawal.legacy;

import com.wezaam.withdrawal.withdrawal.dto.WithdrawalDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.wezaam.withdrawal.withdrawal.dto.WithdrawalFactory.createWithdrawalDTOsFromWithdrawals;

/**
 * Service responsible for retrieving legacy withdrawal records
 * from the old system. Used for backward compatibility.
 */
@Slf4j
@Service
public class WithdrawalLegacyService {

    @Resource
    private WithdrawalRepository withdrawalRepository;

    /**
     * Retrieves all legacy withdrawal entities,
     * converts them to DTOs, and returns them.
     *
     * @return {@link List} of {@link WithdrawalDTO} representing legacy data
     */
    public List<WithdrawalDTO> findAll() {
        log.info("Entering findAll");
        List<Withdrawal> withdrawals = withdrawalRepository.findAll();
        return createWithdrawalDTOsFromWithdrawals(withdrawals);
    }

}
