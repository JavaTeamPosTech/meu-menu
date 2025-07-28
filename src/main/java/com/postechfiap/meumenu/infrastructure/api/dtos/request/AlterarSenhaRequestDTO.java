package com.postechfiap.meumenu.infrastructure.api.dtos.request;

import com.postechfiap.meumenu.core.dtos.usuario.AlterarSenhaInputModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Schema(description = "DTO para requisição de Alteração de Senha")
public record AlterarSenhaRequestDTO(
        @Schema(description = "Senha antiga do usuário", example = "SenhaForte123!")
        @NotBlank(message = "Senha antiga é obrigatória")
        String senhaAntiga,

        @Schema(description = "Nova senha do usuário", example = "SenhaForte321!")
        @NotBlank(message = "Nova senha é obrigatória")
        @Size(min = 6, max = 100, message = "Nova senha deve ter no mínimo 6 caracteres")
        String novaSenha
) {
    public AlterarSenhaInputModel toInputModel(UUID usuarioId) {
        return new AlterarSenhaInputModel(usuarioId, senhaAntiga, novaSenha);
    }
}