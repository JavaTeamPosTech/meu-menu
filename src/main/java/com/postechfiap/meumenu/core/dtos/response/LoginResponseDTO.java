package com.postechfiap.meumenu.core.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para resposta de operação de Login (contém o JWT)")
public record LoginResponseDTO(
        @Schema(description = "Token JWT gerado após login bem-sucedido")
        String token,

        @Schema(description = "Mensagem de status da operação")
        String message,

        @Schema(description = "Status da operação (ex: SUCCESS, FAIL)")
        String status
) {
}