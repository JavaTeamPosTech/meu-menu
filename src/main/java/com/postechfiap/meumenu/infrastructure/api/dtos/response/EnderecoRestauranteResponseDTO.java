package com.postechfiap.meumenu.infrastructure.api.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "DTO para resposta de detalhes de Endereço de Restaurante")
public record EnderecoRestauranteResponseDTO(
        @Schema(description = "ID único do endereço do restaurante")
        UUID id,

        @Schema(description = "Estado do endereço")
        String estado,

        @Schema(description = "Cidade do endereço")
        String cidade,

        @Schema(description = "Bairro do endereço")
        String bairro,

        @Schema(description = "Rua do endereço")
        String rua,

        @Schema(description = "Número do endereço")
        Integer numero,

        @Schema(description = "Complemento do endereço (opcional)")
        String complemento,

        @Schema(description = "CEP do endereço")
        String cep
) {
}