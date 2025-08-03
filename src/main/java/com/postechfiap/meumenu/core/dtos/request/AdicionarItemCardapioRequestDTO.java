package com.postechfiap.meumenu.core.dtos.request;

import com.postechfiap.meumenu.core.dtos.restaurante.item.ItemCardapioInputModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "DTO para requisição de adição de um novo Item de Cardápio")
public record AdicionarItemCardapioRequestDTO(
        @Schema(description = "Nome do item no cardápio", example = "Pizza Margherita")
        @NotBlank(message = "Nome do item é obrigatório")
        @Size(min = 2, max = 100, message = "Nome do item deve ter entre 2 e 100 caracteres")
        String nome,

        @Schema(description = "Descrição do item", example = "Molho de tomate, mussarela, manjericão fresco e azeite extra virgem.")
        @NotBlank(message = "Descrição é obrigatória")
        @Size(min = 5, max = 500, message = "Descrição deve ter entre 5 e 500 caracteres")
        String descricao,

        @Schema(description = "Preço do item", example = "45.90")
        @NotNull(message = "Preço é obrigatório")
        @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
        BigDecimal preco,

        @Schema(description = "Indica se o item está disponível apenas no restaurante (não para delivery)", example = "false")
        @NotNull(message = "Disponibilidade é obrigatória")
        Boolean disponivelApenasNoRestaurante,

        @Schema(description = "URL da foto do item (opcional)", example = "http://example.com/pizza_margherita.jpg")
        String urlFoto
) {
    public ItemCardapioInputModel toInputModel() {
        return new ItemCardapioInputModel(
                nome,
                descricao,
                preco,
                disponivelApenasNoRestaurante,
                urlFoto
        );
    }
}