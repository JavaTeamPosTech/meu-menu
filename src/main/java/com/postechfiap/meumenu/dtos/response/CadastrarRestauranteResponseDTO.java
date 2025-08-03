package com.postechfiap.meumenu.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(description = "DTO para resposta de cadastro de um Restaurante")
public record CadastrarRestauranteResponseDTO(
        @Schema(description = "ID único do restaurante cadastrado")
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

        @Schema(description = "ID do proprietário do restaurante")
        UUID proprietarioId,

        @Schema(description = "Endereço do restaurante")
        EnderecoRestauranteResponseDTO endereco,

        @Schema(description = "Tipos de cozinha do restaurante")
        List<TipoCozinhaResponseDTO> tiposCozinha,

        @Schema(description = "Horários de funcionamento do restaurante")
        List<HorarioFuncionamentoResponseDTO> horariosFuncionamento,

        @Schema(description = "Itens do cardápio do restaurante (no cadastro inicial, pode estar vazia)")
        List<ItemCardapioResponseDTO> itensCardapio, // Mesmo que vazio no cadastro, o campo existe

        @Schema(description = "Mensagem de status da operação")
        String message,

        @Schema(description = "Status da operação (ex: SUCCESS, FAIL)")
        String status
) {
}