package br.com.db.rapid_food_api.vendors.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CNPJ;

public record CreateVendorRequest(
        @NotBlank(message = "O nome do restaurante é obrigatório.")
        String name,

        @NotBlank(message = "O CNPJ é obrigatório.")
        @CNPJ(message = "Formato de CNPJ inválido.")
        String cnpj
) {}