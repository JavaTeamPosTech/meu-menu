package com.postechfiap.meumenu.dtos.request;

import com.postechfiap.meumenu.core.dtos.usuario.LoginInputModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para requisição de Login (autenticação)")
public record LoginRequestDTO(
        @Schema(description = "Login (nome de usuário) do usuário", example = "joao.silva")
        @NotBlank(message = "Login é obrigatório")
        @Size(min = 4, max = 50, message = "Login deve ter entre 4 e 50 caracteres")
        String login,

        @Schema(description = "Senha do usuário", example = "SenhaForte123!")
        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, max = 100, message = "Senha deve ter no mínimo 6 caracteres")
        String senha
) {
    public LoginInputModel toInputModel() {
        return new LoginInputModel(login, senha);
    }
}