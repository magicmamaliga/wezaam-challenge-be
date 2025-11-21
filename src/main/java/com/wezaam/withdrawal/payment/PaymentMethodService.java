package com.wezaam.withdrawal.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class PaymentMethodService {

    @Resource
    private PaymentMethodRepository paymentMethodRepository;

    public PaymentMethod findById(Long paymentMethodId) throws PaymentMethodException {
        log.info("Entering findById with paymentMethodId {}", paymentMethodId);
        return  paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> new PaymentMethodException("Payment method not found"));
    }

}
