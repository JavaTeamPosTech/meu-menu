package com.postechfiap.meumenu.core.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para resposta de deleção de Cliente")
public record DeletarClienteResponseDTO(
        String message,
        String status
) {
}