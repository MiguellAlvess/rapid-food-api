package br.com.db.rapid_food_api.order.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderRequestDto(
        UUID userId,
        UUID vendorId,
        BigDecimal totalAmount,
        List<OrderItemRequestDto> items
) {
}
