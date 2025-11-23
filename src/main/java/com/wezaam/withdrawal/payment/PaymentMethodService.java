package com.wezaam.withdrawal.payment;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * Service responsible for managing payment methods.
 */
@Slf4j
@Service
public class PaymentMethodService {

    @Resource
    private PaymentMethodRepository paymentMethodRepository;


    /**
     * Retrieves a {@link PaymentMethod} by its ID.
     *
     * @param paymentMethodId the ID of the payment method to retrieve
     * @return the {@link PaymentMethod} if found
     * @throws PaymentMethodException if no payment method exists with the specified ID
     */
    public PaymentMethod findById(Long paymentMethodId) {
        log.info("Entering findById with paymentMethodId {}", paymentMethodId);
        return paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> new PaymentMethodException("Payment method not found"));
    }

}
