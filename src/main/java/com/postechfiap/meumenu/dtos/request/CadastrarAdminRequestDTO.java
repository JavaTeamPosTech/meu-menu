package com.postechfiap.meumenu.dtos.request;

import com.postechfiap.meumenu.core.dtos.admin.CadastrarAdminInputModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para requisição de cadastro de um novo Administrador")
public record CadastrarAdminRequestDTO(
        @Schema(description = "Nome completo do administrador. Precisa estar preenchido.", example = "Admin Master")
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String nome,

        @Schema(description = "Endereço de e-mail do administrador. Precisa estar preenchido e ser válido.", example = "admin@meumenu.com")
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
        String email,

        @Schema(description = "Login para acesso. Precisa estar preenchido.", example = "admin.master")
        @NotBlank(message = "Login é obrigatório")
        @Size(min = 4, max = 50, message = "O login deve ter entre 4 e 50 caracteres")
        String login,

        @Schema(description = "Senha do administrador. Precisa estar preenchida e ser forte.", example = "AdminSenhaForte123!")
        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, max = 100, message = "A senha deve ter entre 6 e 100 caracteres")
        String senha
) {
    public CadastrarAdminInputModel toInputModel() {
        return new CadastrarAdminInputModel(
                nome,
                email,
                login,
                senha
        );
    }
}