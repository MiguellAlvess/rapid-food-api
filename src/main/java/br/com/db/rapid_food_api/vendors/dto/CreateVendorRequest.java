package br.com.db.rapid_food_api.vendors.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CNPJ;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Payload para criação de um novo restaurante parceiro")
public record CreateVendorRequest(
        
        @Schema(description = "Nome de restaurante", example = "Pizzaria italiana")
        @NotBlank(message = "O nome do restaurante é obrigatório.")
        String name,

        @Schema(description = "CNPJ válido do restaurante (Apenas números)", example = "06990590000123")
        @NotBlank(message = "O CNPJ é obrigatório.")
        @CNPJ(message = "Formato de CNPJ inválido.")
        String cnpj
) {}