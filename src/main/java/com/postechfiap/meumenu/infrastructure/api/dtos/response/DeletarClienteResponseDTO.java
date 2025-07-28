package com.postechfiap.meumenu.infrastructure.api.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para resposta de deleção de Cliente")
public record DeletarClienteResponseDTO(
        String message,
        String status
) {
}