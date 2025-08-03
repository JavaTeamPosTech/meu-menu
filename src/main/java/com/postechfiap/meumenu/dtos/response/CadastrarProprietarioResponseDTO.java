package com.postechfiap.meumenu.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO para resposta de cadastro de um Proprietário")
public record CadastrarProprietarioResponseDTO(
        @Schema(description = "ID único do proprietário cadastrado")
        UUID id,

        @Schema(description = "Nome do proprietário")
        String nome,

        @Schema(description = "Email do proprietário")
        String email,

        @Schema(description = "Login do proprietário")
        String login,

        @Schema(description = "Data de criação do registro do proprietário")
        LocalDateTime dataCriacao,

        @Schema(description = "Mensagem de status da operação")
        String message,

        @Schema(description = "Status da operação (ex: SUCCESS, FAIL)")
        String status
) {
}