package com.wezaam.withdrawal.withdrawal;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api
@Slf4j
@RestController("/withdrawals")
public class WithdrawalController {

    @Resource
    private WithdrawalService withdrawalService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WithdrawalDTO scheduleWithdrawalRequest(@Valid WithdrawalRequestDTO withdrawalRequestDTO) {
        log.info("Entering scheduleWithdrawalRequest with withdrawalRequestDTO: {}", withdrawalRequestDTO);
        WithdrawalDTO result = withdrawalService.schedule(withdrawalRequestDTO);
        log.info("Exiting scheduleWithdrawalRequest with result: {}", result);
        return result;
    }

    @GetMapping
    public List<WithdrawalDTO> findAll() {
        log.info("Entering findAll");
        return withdrawalService.findAll();
    }
}
