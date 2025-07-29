package com.postechfiap.meumenu.infrastructure.api.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(description = "DTO para resposta de detalhes de Restaurante")
public record RestauranteResponseDTO(
        @Schema(description = "ID único do restaurante")
        UUID id,

        @Schema(description = "CNPJ do restaurante")
        String cnpj,

        @Schema(description = "Razão social do restaurante")
        String razaoSocial,

        @Schema(description = "Nome fantasia do restaurante")
        String nomeFantasia,

        @Schema(description = "Inscrição estadual do restaurante")
        String inscricaoEstadual,

        @Schema(description = "Telefone comercial do restaurante")
        String telefoneComercial,

        @Schema(description = "Endereço do restaurante")
        EnderecoRestauranteResponseDTO endereco,

        @Schema(description = "Tipos de cozinha do restaurante")
        List<TipoCozinhaResponseDTO> tiposCozinha,

        @Schema(description = "Horários de funcionamento do restaurante")
        List<HorarioFuncionamentoResponseDTO> horariosFuncionamento,

        @Schema(description = "Itens do cardápio do restaurante (pode ser nulo ou vazio na listagem geral)")
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        List<ItemCardapioResponseDTO> itensCardapio
) {
}