package com.postechfiap.meumenu.infrastructure.api.dtos.request;

import com.postechfiap.meumenu.core.dtos.restaurante.tipocozinha.TipoCozinhaInputModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para requisição de Tipo de Cozinha")
public record TipoCozinhaRequestDTO(
        @Schema(description = "Nome do tipo de cozinha", example = "Italiana")
        @NotBlank(message = "Nome do tipo de cozinha é obrigatório")
        @Size(min = 2, max = 50, message = "Nome do tipo de cozinha deve ter entre 2 e 50 caracteres")
        String nome
) {
    public TipoCozinhaInputModel toInputModel() {
        return new TipoCozinhaInputModel(nome);
    }
}