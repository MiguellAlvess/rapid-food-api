package br.com.db.rapid_food_api.product.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductRequestDto (
        UUID vendorId,
        String productName,
        String description,
        BigDecimal price
){
}
