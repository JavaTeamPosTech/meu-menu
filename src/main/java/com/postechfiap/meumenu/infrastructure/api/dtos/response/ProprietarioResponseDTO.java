    package com.postechfiap.meumenu.infrastructure.api.dtos.response;

    import com.postechfiap.meumenu.core.domain.entities.enums.StatusContaEnum;
    import io.swagger.v3.oas.annotations.media.Schema;

    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.UUID;

    @Schema(description = "DTO para resposta com detalhes completos de um Proprietário")
    public record ProprietarioResponseDTO(
            UUID id,
            String nome,
            String cpf,
            String email,
            String login,
            String whatsapp,
            StatusContaEnum statusConta,
            LocalDateTime dataCriacao,
            LocalDateTime dataAtualizacao,
            List<EnderecoResponseDTO> enderecos
    ) {
    }