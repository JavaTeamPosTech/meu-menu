package com.postechfiap.meumenu.core.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para resposta de deleção de Restaurante")
public record DeletarRestauranteResponseDTO(
        @Schema(description = "Mensagem de status da operação")
        String message,
        @Schema(description = "Status da operação (ex: SUCCESS, FAIL)")
        String status
) {
}