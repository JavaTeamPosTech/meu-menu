package com.postechfiap.meumenu.dtos.request;

import com.postechfiap.meumenu.core.dtos.endereco.EnderecoInputModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para requisição de endereço")
public record EnderecoRequestDTO(

        @Schema(description = "Estado do endereço. Precisa estar preenchido.", example = "SP")
        @NotBlank(message = "Estado é obrigatório")
        @Size(min = 2, max = 2, message = "O estado deve conter exatamente 2 caracteres")
        String estado,

        @Schema(description = "Cidade do endereço. Precisa estar preenchida.", example = "Sao Paulo")
        @NotBlank(message = "Cidade é obrigatória")
        @Size(max = 100, message = "A cidade deve ter no máximo 100 caracteres")
        String cidade,

        @Schema(description = "Bairro do endereço. Precisa estar preenchido.", example = "Jardim Paulista")
        @NotBlank(message = "Bairro é obrigatório")
        @Size(max = 100, message = "O bairro deve ter no máximo 100 caracteres")
        String bairro,

        @Schema(description = "Rua do endereço. Precisa estar preenchida.", example = "Avenida Paulista")
        @NotBlank(message = "Rua é obrigatória")
        @Size(max = 150, message = "A rua deve ter no máximo 150 caracteres")
        String rua,

        @Schema(description = "Número do endereço. Precisa estar preenchido e ser positivo.", example = "123")
        @NotNull(message = "Número é obrigatório")
        @Positive(message = "O número deve ser positivo")
        Integer numero,

        @Schema(description = "Complemento do endereço (opcional).", example = "Apt 101")
        // @Nullable ou sem anotações de NotBlank/NotNull para permitir que seja opcional
        String complemento,

        @Schema(description = "CEP do endereço no formato 00000-000. Precisa estar preenchido.", example = "01311-000")
        @NotBlank(message = "CEP é obrigatório")
        @Pattern(regexp = "^\\d{5}-\\d{3}$", message = "O CEP deve estar no formato 00000-000")
        String cep
) {
    public EnderecoInputModel toInputModel() {
        return new EnderecoInputModel(
                estado,
                cidade,
                bairro,
                rua,
                numero,
                complemento,
                cep
        );
    }
}