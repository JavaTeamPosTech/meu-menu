package com.postechfiap.meumenu.core.dtos.request;

import com.postechfiap.meumenu.core.dtos.restaurante.endereco.EnderecoRestauranteInputModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para requisição de Endereço de Restaurante")
public record EnderecoRestauranteRequestDTO(
        @Schema(description = "Estado do endereço (UF, 2 caracteres)", example = "SP")
        @NotBlank(message = "Estado é obrigatório")
        @Size(min = 2, max = 2, message = "Estado deve ter 2 caracteres")
        String estado,

        @Schema(description = "Cidade do endereço", example = "São Paulo")
        @NotBlank(message = "Cidade é obrigatória")
        @Size(max = 100, message = "Cidade deve ter no máximo 100 caracteres")
        String cidade,

        @Schema(description = "Bairro do endereço", example = "Centro")
        @NotBlank(message = "Bairro é obrigatório")
        @Size(max = 100, message = "Bairro deve ter no máximo 100 caracteres")
        String bairro,

        @Schema(description = "Rua do endereço", example = "Rua da Paz")
        @NotBlank(message = "Rua é obrigatória")
        @Size(max = 150, message = "Rua deve ter no máximo 150 caracteres")
        String rua,

        @Schema(description = "Número do endereço", example = "123")
        @NotNull(message = "Número é obrigatório")
        @Positive(message = "Número deve ser positivo")
        Integer numero,

        @Schema(description = "Complemento do endereço (opcional)", example = "Sala 101")
        String complemento,

        @Schema(description = "CEP do endereço (formato 00000-000)", example = "01000-000")
        @NotBlank(message = "CEP é obrigatório")
        @Pattern(regexp = "^\\d{5}-\\d{3}$", message = "CEP deve estar no formato 00000-000")
        String cep
) {
    public EnderecoRestauranteInputModel toInputModel() {
        return new EnderecoRestauranteInputModel(
                estado,
                cidade,
                bairro,
                rua,
                numero,
                complemento,
                cep != null ? cep.replace("-", "") : null
        );
    }
}