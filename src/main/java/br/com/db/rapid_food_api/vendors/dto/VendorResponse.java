package br.com.db.rapid_food_api.vendors.dto;

import java.util.UUID;

public record VendorResponse(
        UUID id,
        String name,
        String cnpj,
        Boolean active
) {}