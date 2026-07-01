package br.com.db.rapid_food_api.payment.dto;

import java.util.UUID;

import br.com.db.rapid_food_api.payment.domain.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record CreatePaymentRequest(
        @NotNull(message = "O ID do pedido é obrigatório") UUID orderId,
        @NotNull(message = "O método de pagamento é obrigatório") PaymentMethod paymentMethod) {
}
