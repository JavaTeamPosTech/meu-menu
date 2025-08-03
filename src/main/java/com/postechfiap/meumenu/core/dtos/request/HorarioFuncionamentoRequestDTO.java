package com.postechfiap.meumenu.core.dtos.request;

import com.postechfiap.meumenu.core.domain.entities.enums.DiaSemanaEnum;
import com.postechfiap.meumenu.core.dtos.restaurante.horario.HorarioFuncionamentoInputModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

@Schema(description = "DTO para requisição de Horário de Funcionamento de Restaurante")
public record HorarioFuncionamentoRequestDTO(
        @Schema(description = "Horário de abertura (HH:MM). Formato 24h.", example = "11:00")
        @NotNull(message = "Horário de abertura é obrigatório")
        LocalTime abre,

        @Schema(description = "Horário de fechamento (HH:MM). Formato 24h.", example = "22:00")
        @NotNull(message = "Horário de fechamento é obrigatório")
        LocalTime fecha,

        @Schema(description = "Dia da semana", example = "SEGUNDA_FEIRA", allowableValues = {"SEGUNDA_FEIRA", "TERCA_FEIRA", "QUARTA_FEIRA", "QUINTA_FEIRA", "SEXTA_FEIRA", "SABADO", "DOMINGO"})
        @NotNull(message = "Dia da semana é obrigatório")
        DiaSemanaEnum diaSemana
) {
    public HorarioFuncionamentoInputModel toInputModel() {
        return new HorarioFuncionamentoInputModel(abre, fecha, diaSemana);
    }
}