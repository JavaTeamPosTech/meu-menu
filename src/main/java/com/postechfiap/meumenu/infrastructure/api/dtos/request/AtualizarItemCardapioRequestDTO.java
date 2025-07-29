package com.postechfiap.meumenu.infrastructure.api.dtos.request;

import com.postechfiap.meumenu.core.dtos.restaurante.item.ItemCardapioInputModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "DTO para requisição de atualização de um Item de Cardápio")
public record AtualizarItemCardapioRequestDTO(
        @Schema(description = "Nome do item no cardápio", example = "Pizza Margherita Especial")
        @NotBlank(message = "Nome do item é obrigatório")
        @Size(min = 2, max = 100, message = "Nome do item deve ter entre 2 e 100 caracteres")
        String nome,

        @Schema(description = "Descrição do item", example = "Molho, mussarela, manjericão, azeite e um toque de parmesão.")
        @NotBlank(message = "Descrição é obrigatória")
        @Size(min = 5, max = 500, message = "Descrição deve ter entre 5 e 500 caracteres")
        String descricao,

        @Schema(description = "Preço do item", example = "49.90")
        @NotNull(message = "Preço é obrigatório")
        @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
        BigDecimal preco,

        @Schema(description = "Indica se o item está disponível apenas no restaurante (false se disponível para delivery)", example = "true")
        @NotNull(message = "Disponibilidade é obrigatória")
        Boolean disponivelApenasNoRestaurante,

        @Schema(description = "URL da foto do item (opcional)", example = "http://example.com/pizza_margherita_esp.jpg")
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