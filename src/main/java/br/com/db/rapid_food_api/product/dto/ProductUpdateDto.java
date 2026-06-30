package br.com.db.rapid_food_api.product.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductUpdateDto(UUID id, String description, BigDecimal price) {
}
