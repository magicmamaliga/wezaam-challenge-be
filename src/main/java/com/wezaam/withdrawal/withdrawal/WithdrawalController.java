package com.wezaam.withdrawal.withdrawal;

import com.wezaam.withdrawal.withdrawal.dto.WithdrawalDTO;
import com.wezaam.withdrawal.withdrawal.dto.WithdrawalRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;

/**
 * REST controller that handles Withdrawal API requests.
 */
@Slf4j
@RestController
@RequestMapping("/withdrawals")
@Tag(name = "Withdrawal API", description = "Operations related to withdrawals")
public class WithdrawalController {

    @Resource
    private WithdrawalService withdrawalService;


    /**
     * Creates and schedules a new withdrawal request.
     *
     * @param withdrawalRequestDTO request details for the withdrawal, must be valid
     * @return the created {@link WithdrawalDTO}
     */
    @Operation(summary = "Schedule a withdrawal request", description = "Creates a new scheduled withdrawal")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WithdrawalDTO scheduleWithdrawalRequest(@Valid WithdrawalRequestDTO withdrawalRequestDTO) {
        log.info("Entering scheduleWithdrawalRequest with withdrawalRequestDTO: {}", withdrawalRequestDTO);
        WithdrawalDTO result = withdrawalService.schedule(withdrawalRequestDTO);
        log.info("Exiting scheduleWithdrawalRequest with result: {}", result);
        return result;
    }


    /**
     * Retrieves all withdrawal records.
     *
     * @return a {@link List} of {@link WithdrawalDTO}
     */
    @Operation(summary = "Find all withdrawals")
    @GetMapping
    public List<WithdrawalDTO> findAll() {
        log.info("Entering findAll");
        return withdrawalService.findAll();
    }
}
