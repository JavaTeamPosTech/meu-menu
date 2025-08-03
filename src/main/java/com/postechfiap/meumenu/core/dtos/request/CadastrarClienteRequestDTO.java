package com.postechfiap.meumenu.core.dtos.request;

import com.postechfiap.meumenu.core.domain.entities.enums.AlergiaAlimentarEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.GeneroEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.MetodoPagamentoEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.TiposComidaEnum;
import com.postechfiap.meumenu.core.dtos.endereco.EnderecoInputModel;
import com.postechfiap.meumenu.core.dtos.cliente.CadastrarClienteInputModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Schema(description = "DTO para requisição de cadastro de um novo Cliente")
public record CadastrarClienteRequestDTO(

        @Schema(description = "Nome completo do cliente. Precisa estar preenchido.", example = "João da Silva")
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String nome,

        @Schema(description = "CPF do cliente (apenas números). Precisa estar preenchido.", example = "12345678900")
        @NotBlank(message = "CPF é obrigatório")
        @Pattern(regexp = "^\\d{11}$", message = "CPF deve conter 11 dígitos numéricos")
        String cpf,

        @Schema(description = "Email do cliente. Precisa estar preenchido e ser válido.", example = "joaodasilva@email.com")
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
        String email,

        @Schema(description = "Login para acesso. Precisa estar preenchido.", example = "joaodasilva")
        @NotBlank(message = "Login é obrigatório")
        @Size(min = 4, max = 50, message = "O login deve ter entre 4 e 50 caracteres")
        String login,

        @Schema(description = "Data de nascimento do cliente (formato AAAA-MM-DD). Precisa estar preenchida.", example = "1990-01-01")
        @NotNull(message = "Data de nascimento é obrigatória")
        @PastOrPresent(message = "Data de nascimento não pode ser futura")
        LocalDate dataNascimento,

        @Schema(description = "Gênero do cliente. Precisa estar preenchido.", example = "MASCULINO")
        @NotNull(message = "Gênero é obrigatório")
        GeneroEnum genero,

        @Schema(description = "Telefone do cliente (apenas números, com ou sem DDI). Precisa estar preenchido.", example = "5581999992345")
        @Pattern(regexp = "^\\+?\\d{8,15}$", message = "Número de telefone inválido")
        @NotBlank(message = "Telefone é obrigatório")
        String telefone,

        @Schema(description = "Preferências alimentares do cliente.", example = "[\"VEGETARIANA\", \"SAUDAVEL\"]")
        Set<TiposComidaEnum> preferenciasAlimentares,

        @Schema(description = "Alergias alimentares do cliente.", example = "[\"AMENDOIM\", \"LACTOSE\"]")
        Set<AlergiaAlimentarEnum> alergias,

        @Schema(description = "Método de pagamento preferido do cliente.", example = "CREDITO")
        MetodoPagamentoEnum metodoPagamentoPreferido,

        @Schema(description = "Indica se o cliente deseja receber notificações. Precisa estar preenchido.", example = "true", defaultValue = "true")
        @NotNull(message = "Notificações ativas é obrigatório")
        Boolean notificacoesAtivas,

        @Schema(description = "Senha do cliente. Precisa estar preenchida e ser forte.", example = "SenhaForte123!")
        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, max = 100, message = "A senha deve ter entre 6 e 100 caracteres")
        String senha,

        @Schema(description = "Lista de endereços do cliente (pelo menos 1).", minLength = 1)
        @NotNull(message = "A lista de endereços não pode ser nula")
        @Size(min = 1, message = "Pelo menos um endereço deve ser informado")
        @Valid
        List<EnderecoRequestDTO> enderecos
) {
    public CadastrarClienteInputModel toInputModel() {
        Set<TiposComidaEnum> safePreferencias = preferenciasAlimentares != null
                ? new HashSet<>(preferenciasAlimentares)
                : new HashSet<>();
        Set<AlergiaAlimentarEnum> safeAlergias = alergias != null
                ? new HashSet<>(alergias)
                : new HashSet<>();
        List<EnderecoInputModel> safeEnderecos = enderecos != null
                ? enderecos.stream().map(EnderecoRequestDTO::toInputModel).collect(Collectors.toList())
                : new ArrayList<>();

        return new CadastrarClienteInputModel(
                nome,
                email,
                login,
                senha,
                cpf,
                dataNascimento,
                genero,
                telefone,
                safePreferencias,
                safeAlergias,
                metodoPagamentoPreferido,
                notificacoesAtivas,
                safeEnderecos
        );
    }
}