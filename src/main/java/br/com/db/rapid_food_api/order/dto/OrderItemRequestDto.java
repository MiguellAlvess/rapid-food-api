package br.com.db.rapid_food_api.order.dto;

import java.math.BigDecimal;

public record OrderItemRequestDto(
        Integer quantity,
        BigDecimal price,
        BigDecimal total,
        String productName
) {
}
