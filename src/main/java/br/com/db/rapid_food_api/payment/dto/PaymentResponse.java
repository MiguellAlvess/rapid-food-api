package br.com.db.rapid_food_api.payment.dto;

import br.com.db.rapid_food_api.payment.domain.enums.PaymentMethod;
import br.com.db.rapid_food_api.payment.domain.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentResponse(
        UUID id,
        UUID orderId,
        BigDecimal amount,
        PaymentStatus status,
        PaymentMethod paymentMethod,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
