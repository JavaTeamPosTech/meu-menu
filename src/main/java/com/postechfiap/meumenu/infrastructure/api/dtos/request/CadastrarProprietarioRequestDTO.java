package com.postechfiap.meumenu.infrastructure.api.dtos.request;

import com.postechfiap.meumenu.core.dtos.endereco.EnderecoInputModel;
import com.postechfiap.meumenu.core.dtos.proprietario.CadastrarProprietarioInputModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "DTO para requisição de cadastro de um novo Proprietário")
public record CadastrarProprietarioRequestDTO(

        @Schema(description = "Nome completo do proprietário. Precisa estar preenchido.", example = "Maria Silva")
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String nome,

        @Schema(description = "Endereço de e-mail do proprietário. Precisa estar preenchido e ser válido.", example = "maria.silva@example.com")
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
        String email,

        @Schema(description = "Login para acesso. Precisa estar preenchido.", example = "maria.silva")
        @NotBlank(message = "Login é obrigatório")
        @Size(min = 4, max = 50, message = "O login deve ter entre 4 e 50 caracteres")
        String login,

        @Schema(description = "Senha do proprietário. Precisa estar preenchida e ser forte.", example = "SenhaForte456!")
        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, max = 100, message = "A senha deve ter entre 6 e 100 caracteres")
        String senha,

        @Schema(description = "CPF do proprietário (apenas números). Precisa estar preenchido.", example = "12345678900")
        @NotBlank(message = "CPF é obrigatório")
        @Pattern(regexp = "^\\d{11}$", message = "CPF deve conter 11 dígitos numéricos")
        String cpf,

        @Schema(description = "Número de WhatsApp do proprietário (apenas números, com ou sem DDI).", example = "5511987654321")
        @Pattern(regexp = "^\\+?\\d{8,15}$", message = "Número de WhatsApp inválido")
        @NotBlank(message = "WhatsApp é obrigatório")
        String whatsapp,

        @Schema(description = "Lista de endereços do proprietário (pelo menos 1).", minLength = 1)
        @NotNull(message = "A lista de endereços não pode ser nula")
        @Size(min = 1, message = "Pelo menos um endereço deve ser informado")
        @Valid
        List<EnderecoRequestDTO> enderecos
) {
    public CadastrarProprietarioInputModel toInputModel() {
        List<EnderecoInputModel> safeEnderecos = enderecos != null
                ? enderecos.stream().map(EnderecoRequestDTO::toInputModel).collect(Collectors.toList())
                : new ArrayList<>();

        return new CadastrarProprietarioInputModel(
                nome,
                email,
                login,
                senha,
                cpf,
                whatsapp,
                safeEnderecos
        );
    }
}