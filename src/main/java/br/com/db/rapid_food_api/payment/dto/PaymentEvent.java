package br.com.db.rapid_food_api.payment.dto;

import java.math.BigDecimal;
import java.util.UUID;

import br.com.db.rapid_food_api.payment.domain.enums.PaymentMethod;

public record PaymentEvent(
        UUID paymentId,
        UUID orderId,
        BigDecimal amount,
        PaymentMethod paymentMethod) {
}
