package com.wezaam.withdrawal.payment;

//This may be needed if transaction would want to send PaymentMethod
public class PaymentMethodDTOFactory {

    public static PaymentMethodDTO paymentMethodToDTO(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            return null;
        }

        return new PaymentMethodDTO(
                paymentMethod.getId(),
                paymentMethod.getUser() != null ? paymentMethod.getUser().getId() : null,
                paymentMethod.getName()
        );
    }
}
