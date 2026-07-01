package br.com.db.rapid_food_api.payment.mapper;

import org.springframework.stereotype.Component;

import br.com.db.rapid_food_api.payment.domain.Payment;
import br.com.db.rapid_food_api.payment.dto.PaymentEvent;
import br.com.db.rapid_food_api.payment.dto.PaymentResponse;

@Component
public class PaymentMapper {

    public PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getPaymentMethod(),
                payment.getCreatedAt(),
                payment.getUpdatedAt());
    }

    public PaymentEvent toEvent(Payment payment) {
        return new PaymentEvent(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getAmount(),
                payment.getPaymentMethod());
    }
}
