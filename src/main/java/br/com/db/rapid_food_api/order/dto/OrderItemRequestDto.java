package br.com.db.rapid_food_api.order.dto;

import java.util.UUID;

public record OrderItemRequestDto(
        Integer quantity,
        UUID productId
) {
}
