package com.wezaam.withdrawal.withdrawal;

import com.wezaam.withdrawal.payment.PaymentMethod;
import com.wezaam.withdrawal.payment.PaymentMethodException;
import com.wezaam.withdrawal.payment.PaymentMethodService;
import com.wezaam.withdrawal.service.EventsService;
import com.wezaam.withdrawal.service.TransactionException;
import com.wezaam.withdrawal.service.WithdrawalProcessingService;
import com.wezaam.withdrawal.user.UserService;
import com.wezaam.withdrawal.withdrawal.legacy.WithdrawalLegacyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.wezaam.withdrawal.withdrawal.WithdrawalFactory.*;

@Slf4j
@Service
public class WithdrawalService {

    @Resource
    private WithdrawalScheduledRepository withdrawalScheduledRepository;
    @Resource
    private WithdrawalProcessingService withdrawalProcessingService;
    @Resource
    private EventsService eventsService;
    @Resource
    private PaymentMethodService paymentMethodService;
    @Resource
    private UserService userService;
    @Resource
    private WithdrawalLegacyService withdrawalLegacyService;

    @Transactional
    public WithdrawalDTO schedule(WithdrawalRequestDTO withdrawalRequestDTO) {
        log.info("Entering schedule with withdrawalRequestDTO: {}", withdrawalRequestDTO);
        userService.findById(withdrawalRequestDTO.userId());
        paymentMethodService.findById(withdrawalRequestDTO.paymentMethodId());
        WithdrawalScheduled withdrawalScheduled =
                withdrawalScheduledRepository.save(createFromRequest(withdrawalRequestDTO));
        return createWithdrawalDTOFromWithdrawal(withdrawalScheduled);
    }

    public List<WithdrawalDTO> findAll() {
        log.info("Entering findAll()");
        List<WithdrawalDTO> withdrawalDTOs = new ArrayList<>();
        withdrawalDTOs.addAll(withdrawalLegacyService.findAll());
        withdrawalDTOs.addAll(createWithdrawalDTOsFromWithdrawalSchedules(withdrawalScheduledRepository.findAll()));
        return withdrawalDTOs;
    }

    @Scheduled(fixedDelay = 5000)
    public void run() {
        log.info("Process scheduled withdrawals");
        withdrawalScheduledRepository.findAllByExecuteAtBefore(Instant.now()).forEach(this::processScheduled);
    }

    private void processScheduled(WithdrawalScheduled withdrawal) {

        PaymentMethod paymentMethod;
        try {
            paymentMethod = paymentMethodService.findById(withdrawal.getPaymentMethodId());
        } catch (PaymentMethodException e) {
            log.error("PaymentMethod not found, will retry in the next run, for withdrawal: {}, errorMessage: {}",
                    withdrawal, e.getMessage());
            return;
        }

        try {
            var transactionId =
                    withdrawalProcessingService.sendToProcessing(withdrawal.getAmount(),
                            paymentMethod);
            withdrawal.setStatus(WithdrawalStatus.PROCESSING);
            withdrawal.setTransactionId(transactionId);
        } catch (TransactionException e) {
            withdrawal.setStatus(WithdrawalStatus.FAILED);
        } catch (Exception e) {
            withdrawal.setStatus(WithdrawalStatus.INTERNAL_ERROR);
        }
        eventsService.send(withdrawal);
        withdrawalScheduledRepository.save(withdrawal);
    }

}
