package br.com.db.rapid_food_api.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductUpdateDto(@NotNull UUID id, String description, BigDecimal price) {
}
