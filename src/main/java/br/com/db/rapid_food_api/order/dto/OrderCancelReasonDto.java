package br.com.db.rapid_food_api.order.dto;

import jakarta.validation.constraints.NotBlank;

public record OrderCancelReasonDto(@NotBlank String reason) {
}
