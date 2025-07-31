package com.postechfiap.meumenu.infrastructure.api.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO para resposta de cadastro de um Administrador")
public record CadastrarAdminResponseDTO(
        @Schema(description = "ID único do administrador cadastrado")
        UUID id,

        @Schema(description = "Nome completo do administrador")
        String nome,

        @Schema(description = "Endereço de e-mail do administrador")
        String email,

        @Schema(description = "Login do administrador")
        String login,

        @Schema(description = "Data de criação do registro do administrador")
        LocalDateTime dataCriacao,

        @Schema(description = "Mensagem de status da operação")
        String message,

        @Schema(description = "Status da operação (ex: SUCCESS, FAIL)")
        String status
) {
}