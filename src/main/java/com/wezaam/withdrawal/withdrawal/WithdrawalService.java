package com.wezaam.withdrawal.withdrawal;

import com.wezaam.withdrawal.service.TransactionException;
import com.wezaam.withdrawal.payment.PaymentMethod;
import com.wezaam.withdrawal.payment.PaymentMethodRepository;
import com.wezaam.withdrawal.service.EventsService;
import com.wezaam.withdrawal.service.WithdrawalProcessingService;
import com.wezaam.withdrawal.withdrawal.scheduled.WithdrawalScheduled;
import com.wezaam.withdrawal.withdrawal.scheduled.WithdrawalScheduledRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class WithdrawalService {

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    @Resource
    private WithdrawalRepository withdrawalRepository;
    @Resource
    private WithdrawalScheduledRepository withdrawalScheduledRepository;
    @Resource
    private WithdrawalProcessingService withdrawalProcessingService;
    @Resource
    private PaymentMethodRepository paymentMethodRepository;
    @Resource
    private EventsService eventsService;

    public void create(Withdrawal withdrawal) {
        Withdrawal pendingWithdrawal = withdrawalRepository.save(withdrawal);

        executorService.submit(() -> {
            Optional<Withdrawal> savedWithdrawalOptional = withdrawalRepository.findById(pendingWithdrawal.getId());

            PaymentMethod paymentMethod;
            if (savedWithdrawalOptional.isPresent()) {
                paymentMethod = paymentMethodRepository.findById(savedWithdrawalOptional.get().getPaymentMethodId()).orElse(null);
            } else {
                paymentMethod = null;
            }

            if (savedWithdrawalOptional.isPresent() && paymentMethod != null) {
                Withdrawal savedWithdrawal = savedWithdrawalOptional.get();
                try {
                    var transactionId = withdrawalProcessingService.sendToProcessing(withdrawal.getAmount(), paymentMethod);
                    savedWithdrawal.setStatus(WithdrawalStatus.PROCESSING);
                    savedWithdrawal.setTransactionId(transactionId);
                    withdrawalRepository.save(savedWithdrawal);
                    eventsService.send(savedWithdrawal);
                } catch (Exception e) {
                    if (e instanceof TransactionException) {
                        savedWithdrawal.setStatus(WithdrawalStatus.FAILED);
                        withdrawalRepository.save(savedWithdrawal);
                        eventsService.send(savedWithdrawal);
                    } else {
                        savedWithdrawal.setStatus(WithdrawalStatus.INTERNAL_ERROR);
                        withdrawalRepository.save(savedWithdrawal);
                        eventsService.send(savedWithdrawal);
                    }
                }
            }
        });
    }

    public void schedule(WithdrawalScheduled withdrawalScheduled) {
        withdrawalScheduledRepository.save(withdrawalScheduled);
    }

    @Scheduled(fixedDelay = 5000)
    public void run() {
        withdrawalScheduledRepository.findAllByExecuteAtBefore(Instant.now())
                .forEach(this::processScheduled);
    }

    private void processScheduled(WithdrawalScheduled withdrawal) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(withdrawal.getPaymentMethodId()).orElse(null);
        if (paymentMethod != null) {
            try {
                var transactionId = withdrawalProcessingService.sendToProcessing(withdrawal.getAmount(), paymentMethod);
                withdrawal.setStatus(WithdrawalStatus.PROCESSING);
                withdrawal.setTransactionId(transactionId);
                withdrawalScheduledRepository.save(withdrawal);
                eventsService.send(withdrawal);
            } catch (Exception e) {
                if (e instanceof TransactionException) {
                    withdrawal.setStatus(WithdrawalStatus.FAILED);
                    withdrawalScheduledRepository.save(withdrawal);
                    eventsService.send(withdrawal);
                } else {
                    withdrawal.setStatus(WithdrawalStatus.INTERNAL_ERROR);
                    withdrawalScheduledRepository.save(withdrawal);
                    eventsService.send(withdrawal);
                }
            }
        }
    }
}
