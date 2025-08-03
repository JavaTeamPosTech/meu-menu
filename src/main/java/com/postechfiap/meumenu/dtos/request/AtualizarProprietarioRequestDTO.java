package com.postechfiap.meumenu.dtos.request;

import com.postechfiap.meumenu.core.domain.entities.enums.StatusContaEnum;
import com.postechfiap.meumenu.core.dtos.proprietario.AtualizarProprietarioInputModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.UUID;

@Schema(description = "DTO para requisição de atualização de Proprietário")
public record AtualizarProprietarioRequestDTO(
        @Schema(description = "Nome completo do proprietário", example = "Maria Silva Atualizada")
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String nome,

        @Schema(description = "Endereço de e-mail do proprietário", example = "maria.atualizada@example.com")
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
        String email,

        @Schema(description = "Login para acesso ao sistema", example = "maria.atualizada")
        @NotBlank(message = "Login é obrigatório")
        @Size(min = 4, max = 50, message = "Login deve ter entre 4 e 50 caracteres")
        String login,

        @Schema(description = "CPF do proprietário (apenas números)", example = "09876543210")
        @NotBlank(message = "CPF é obrigatório")
        @Pattern(regexp = "^\\d{11}$", message = "CPF deve conter 11 dígitos numéricos")
        String cpf,

        @Schema(description = "Número de WhatsApp do proprietário", example = "5511998765432")
        @Pattern(regexp = "^\\+?\\d{8,15}$", message = "Número de WhatsApp inválido")
        @NotBlank(message = "WhatsApp é obrigatório")
        String whatsapp,

        @Schema(description = "Status da conta do proprietário", example = "ATIVO", allowableValues = {"ATIVO", "BLOQUEADO", "PENDENTE_APROVACAO", "PENDENTE_PAGAMENTO", "DESATIVADO"})
        @NotNull(message = "Status da conta é obrigatório")
        StatusContaEnum statusConta
) {
    public AtualizarProprietarioInputModel toInputModel(UUID id) {
        return new AtualizarProprietarioInputModel(
                id,
                nome,
                email,
                login,
                cpf,
                whatsapp,
                statusConta
        );
    }
}