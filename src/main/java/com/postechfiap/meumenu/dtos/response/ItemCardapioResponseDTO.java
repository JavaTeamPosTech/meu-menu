package com.postechfiap.meumenu.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "DTO para resposta de detalhes de Item do Cardápio")
public record ItemCardapioResponseDTO(
        @Schema(description = "ID único do item do cardápio")
        UUID id,

        @Schema(description = "Nome do item do cardápio")
        String nome,

        @Schema(description = "Descrição do item do cardápio")
        String descricao,

        @Schema(description = "Preço do item do cardápio")
        BigDecimal preco,

        @Schema(description = "Disponibilidade para pedir apenas no restaurante (false se disponível para delivery)")
        Boolean disponivelApenasNoRestaurante,

        @Schema(description = "URL ou caminho da foto do prato")
        String urlFoto
) {
}