package com.wezaam.withdrawal.withdrawal;

import com.wezaam.withdrawal.withdrawal.dto.WithdrawalDTO;
import com.wezaam.withdrawal.withdrawal.dto.WithdrawalRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/withdrawals")
@Tag(name = "Withdrawal API", description = "Operations related to withdrawals")
public class WithdrawalController {

    @Resource
    private WithdrawalService withdrawalService;

    @Operation(summary = "Schedule a withdrawal request", description = "Creates a new scheduled withdrawal")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WithdrawalDTO scheduleWithdrawalRequest(@Valid WithdrawalRequestDTO withdrawalRequestDTO) {
        log.info("Entering scheduleWithdrawalRequest with withdrawalRequestDTO: {}", withdrawalRequestDTO);
        WithdrawalDTO result = withdrawalService.schedule(withdrawalRequestDTO);
        log.info("Exiting scheduleWithdrawalRequest with result: {}", result);
        return result;
    }

    @Operation(summary = "Find all withdrawals")
    @GetMapping
    public List<WithdrawalDTO> findAll() {
        log.info("Entering findAll");
        return withdrawalService.findAll();
    }
}
