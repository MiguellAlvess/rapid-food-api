package br.com.db.rapid_food_api.product.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponseDto(UUID id, String vendorName, String productName, BigDecimal price,
                                 LocalDateTime createdAt, LocalDateTime updatedAt) {
}
