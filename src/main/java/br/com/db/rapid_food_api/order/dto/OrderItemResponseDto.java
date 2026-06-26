package br.com.db.rapid_food_api.order.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponseDto(
        UUID id,
        Integer quantity,
        BigDecimal price,
        BigDecimal total,
        String productName
) {
}
