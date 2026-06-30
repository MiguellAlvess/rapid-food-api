package br.com.db.rapid_food_api.product.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductRequestDto(@NotNull(message = "O ID do vendedor é obrigatório") UUID vendorId,
                                @NotNull(message = "O Nome do produto é obrigatório") String productName,
                                @NotNull(message = "A Descrição é obrigatória") String description,
                                @NotNull(message = "O Preço é obrigatório") BigDecimal price) {
}
