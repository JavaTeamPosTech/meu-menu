package com.postechfiap.meumenu.infrastructure.api.dtos.request;

import com.postechfiap.meumenu.core.dtos.restaurante.CadastrarRestauranteInputModel;
import com.postechfiap.meumenu.core.dtos.restaurante.horario.HorarioFuncionamentoInputModel;
import com.postechfiap.meumenu.core.dtos.restaurante.tipocozinha.TipoCozinhaInputModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Schema(description = "DTO para requisição de cadastro de um novo Restaurante")
public record CadastrarRestauranteRequestDTO(
        @Schema(description = "CNPJ do restaurante (apenas números)", example = "00000000000100")
        @NotBlank(message = "CNPJ é obrigatório")
        @Pattern(regexp = "^\\d{14}$", message = "CNPJ deve conter 14 dígitos numéricos")
        String cnpj,

        @Schema(description = "Razão social do restaurante", example = "Empresa de Alimentos SA")
        @NotBlank(message = "Razão Social é obrigatória")
        @Size(min = 2, max = 255, message = "Razão Social deve ter entre 2 e 255 caracteres")
        String razaoSocial,

        @Schema(description = "Nome fantasia do restaurante", example = "Restaurante Delícia")
        @NotBlank(message = "Nome Fantasia é obrigatório")
        @Size(min = 2, max = 255, message = "Nome Fantasia deve ter entre 2 e 255 caracteres")
        String nomeFantasia,

        @Schema(description = "Inscrição estadual do restaurante (opcional)", example = "123456789")
        String inscricaoEstadual,

        @Schema(description = "Telefone comercial do restaurante (apenas números, com ou sem DDI)", example = "5511912345678")
        @Pattern(regexp = "^\\+?\\d{8,15}$", message = "Número de telefone inválido")
        @NotBlank(message = "Telefone Comercial é obrigatório")
        String telefoneComercial,

        @Schema(description = "Endereço do restaurante")
        @NotNull(message = "Endereço é obrigatório")
        @Valid
        EnderecoRestauranteRequestDTO endereco,

        @Schema(description = "Tipos de cozinha do restaurante (pelo menos 1)")
        @NotNull(message = "Tipos de Cozinha é obrigatório")
        @Size(min = 1, message = "Pelo menos um Tipo de Cozinha deve ser informado")
        @Valid
        List<TipoCozinhaRequestDTO> tiposCozinha,

        @Schema(description = "Horários de funcionamento do restaurante (pelo menos 1)")
        @NotNull(message = "Horários de Funcionamento é obrigatório")
        @Size(min = 1, message = "Pelo menos um Horário de Funcionamento deve ser informado")
        @Valid
        List<HorarioFuncionamentoRequestDTO> horariosFuncionamento
) {
    public CadastrarRestauranteInputModel toInputModel(UUID proprietarioId) {
        List<TipoCozinhaInputModel> safeTiposCozinha = tiposCozinha != null
                ? tiposCozinha.stream().map(TipoCozinhaRequestDTO::toInputModel).collect(Collectors.toList())
                : new ArrayList<>();
        List<HorarioFuncionamentoInputModel> safeHorarios = horariosFuncionamento != null
                ? horariosFuncionamento.stream().map(HorarioFuncionamentoRequestDTO::toInputModel).collect(Collectors.toList())
                : new ArrayList<>();

        return new CadastrarRestauranteInputModel(
                proprietarioId,
                cnpj,
                razaoSocial,
                nomeFantasia,
                inscricaoEstadual,
                telefoneComercial,
                endereco != null ? endereco.toInputModel() : null,
                safeTiposCozinha,
                safeHorarios
        );
    }
}