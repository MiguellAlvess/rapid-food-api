package br.com.db.rapid_food_api.user.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        Boolean active,
        LocalDateTime createdAt) {
}
