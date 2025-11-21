package com.wezaam.withdrawal.payment;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PaymentMethodService {

    @Resource
    private PaymentMethodRepository paymentMethodRepository;

    public PaymentMethod findById(Long paymentMethodId) throws PaymentMethodException {
        return  paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> new PaymentMethodException("Payment method not found"));
    }
}
