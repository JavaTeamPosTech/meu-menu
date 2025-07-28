package com.postechfiap.meumenu.infrastructure.api.dtos.response;

import com.postechfiap.meumenu.core.domain.entities.enums.DiaSemanaEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;
import java.util.UUID;

@Schema(description = "DTO para resposta de detalhes de Horário de Funcionamento de Restaurante")
public record HorarioFuncionamentoResponseDTO(
        @Schema(description = "ID único do horário de funcionamento")
        UUID id,

        @Schema(description = "Horário de abertura (HH:MM)")
        LocalTime abre,

        @Schema(description = "Horário de fechamento (HH:MM)")
        LocalTime fecha,

        @Schema(description = "Dia da semana")
        DiaSemanaEnum diaSemana
) {
}