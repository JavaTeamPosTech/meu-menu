package com.postechfiap.meumenu.core.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "DTO para resposta de detalhes de Tipo de Cozinha")
public record TipoCozinhaResponseDTO(
        @Schema(description = "ID único do tipo de cozinha")
        UUID id,

        @Schema(description = "Nome do tipo de cozinha")
        String nome
) {
}