package com.postechfiap.meumenu.core.dtos.restaurante.horario;

import com.postechfiap.meumenu.core.domain.entities.enums.DiaSemanaEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HorarioFuncionamentoInputModel {
    private LocalTime abre;
    private LocalTime fecha;
    private DiaSemanaEnum diaSemana;
}