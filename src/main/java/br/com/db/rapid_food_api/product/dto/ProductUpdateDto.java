package br.com.db.rapid_food_api.product.dto;

import java.math.BigDecimal;

public record ProductUpdateDto(
        String description,
        BigDecimal price
) {
}
