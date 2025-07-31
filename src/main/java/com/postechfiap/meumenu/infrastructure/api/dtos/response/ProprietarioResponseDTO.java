    package com.postechfiap.meumenu.infrastructure.api.dtos.response;

    import com.postechfiap.meumenu.core.domain.entities.enums.StatusContaEnum;
    import io.swagger.v3.oas.annotations.media.Schema;

    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.UUID;

    @Schema(description = "DTO para resposta com detalhes completos de um Proprietário")
    public record ProprietarioResponseDTO(
            @Schema(description = "ID único do proprietário")
            UUID id,

            @Schema(description = "Nome completo do proprietário")
            String nome,

            @Schema(description = "CPF do proprietário (apenas números)")
            String cpf,

            @Schema(description = "Endereço de e-mail do proprietário")
            String email,

            @Schema(description = "Login do proprietário")
            String login,

            @Schema(description = "Número de WhatsApp do proprietário")
            String whatsapp,

            @Schema(description = "Status da conta do proprietário")
            StatusContaEnum statusConta,

            @Schema(description = "Data de criação do registro do proprietário")
            LocalDateTime dataCriacao,

            @Schema(description = "Data da última atualização do registro do proprietário")
            LocalDateTime dataAtualizacao,

            @Schema(description = "Lista de endereços do proprietário")
            List<EnderecoResponseDTO> enderecos,

            @Schema(description = "Lista de restaurantes do proprietário")
            List<RestauranteResponseDTO> restaurantes
    ) {
    }