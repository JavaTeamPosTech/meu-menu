package com.postechfiap.meumenu.core.dtos.request;

import com.postechfiap.meumenu.core.domain.entities.enums.AlergiaAlimentarEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.GeneroEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.MetodoPagamentoEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.TiposComidaEnum;
import com.postechfiap.meumenu.core.dtos.cliente.AtualizarClienteInputModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Schema(description = "DTO para requisição de atualização de Cliente")
public record AtualizarClienteRequestDTO(
        @Schema(description = "Nome completo do cliente", example = "João da Silva Atualizado")
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String nome,

        @Schema(description = "Endereço de e-mail do cliente", example = "joao.atualizado@example.com")
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
        String email,

        @Schema(description = "Login para acesso ao sistema", example = "joao.atualizado")
        @NotBlank(message = "Login é obrigatório")
        @Size(min = 4, max = 50, message = "Login deve ter entre 4 e 50 caracteres")
        String login,

        @Schema(description = "CPF do cliente (apenas números)", example = "12345678900")
        @NotBlank(message = "CPF é obrigatório")
        @Pattern(regexp = "^\\d{11}$", message = "CPF deve conter 11 dígitos numéricos")
        String cpf,

        @Schema(description = "Data de nascimento do cliente (formato AAAA-MM-DD)", example = "1990-05-15")
        @NotNull(message = "Data de nascimento é obrigatória")
        @PastOrPresent(message = "Data de nascimento não pode ser futura")
        LocalDate dataNascimento,

        @Schema(description = "Gênero do cliente", example = "MASCULINO", allowableValues = {"MASCULINO", "FEMININO", "OUTRO"})
        @NotNull(message = "Gênero é obrigatório")
        GeneroEnum genero,

        @Schema(description = "Telefone do cliente (apenas números, com ou sem DDI)", example = "11987654321")
        @Pattern(regexp = "^\\+?\\d{8,15}$", message = "Número de telefone inválido")
        @NotBlank(message = "Telefone é obrigatório")
        String telefone,

        @Schema(description = "Preferências alimentares do cliente", example = "[\"ITALIANA\", \"VEGETARIANA\"]")
        Set<TiposComidaEnum> preferenciasAlimentares,

        @Schema(description = "Alergias alimentares do cliente", example = "[\"GLUTEN\", \"LACTOSE\"]")
        Set<AlergiaAlimentarEnum> alergias,

        @Schema(description = "Método de pagamento preferido", example = "CREDITO", allowableValues = {"PIX", "DEBITO", "CREDITO", "DINHEIRO"})
        MetodoPagamentoEnum metodoPagamentoPreferido,

        @Schema(description = "Indica se o cliente deseja receber notificações", example = "true", defaultValue = "true")
        @NotNull(message = "Notificações ativas é obrigatório")
        Boolean notificacoesAtivas
) {
    public AtualizarClienteInputModel toInputModel(UUID id) {
        Set<TiposComidaEnum> safePreferencias = preferenciasAlimentares != null
                ? new HashSet<>(preferenciasAlimentares)
                : new HashSet<>();
        Set<AlergiaAlimentarEnum> safeAlergias = alergias != null
                ? new HashSet<>(alergias)
                : new HashSet<>();

        return new AtualizarClienteInputModel(
                id,
                nome,
                email,
                login,
                cpf,
                dataNascimento,
                genero,
                telefone,
                safePreferencias,
                safeAlergias,
                metodoPagamentoPreferido,
                notificacoesAtivas
        );
    }
}