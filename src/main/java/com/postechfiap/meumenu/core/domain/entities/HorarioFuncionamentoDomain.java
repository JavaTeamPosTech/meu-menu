package com.postechfiap.meumenu.core.domain.entities;

import com.postechfiap.meumenu.core.domain.entities.enums.DiaSemanaEnum; // NOVO: Importar o Enum
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class HorarioFuncionamentoDomain {

    private UUID id;
    private LocalTime abre;
    private LocalTime fecha;
    private DiaSemanaEnum diaSemana;
    private RestauranteDomain restaurante;

    public HorarioFuncionamentoDomain(LocalTime abre, LocalTime fecha, DiaSemanaEnum diaSemana) {
        this.id = UUID.randomUUID();
        this.abre = abre;
        this.fecha = fecha;
        this.diaSemana = diaSemana;
    }

    public HorarioFuncionamentoDomain(UUID id, LocalTime abre, LocalTime fecha, DiaSemanaEnum diaSemana, RestauranteDomain restaurante) {
        this(abre, fecha, diaSemana);
        this.id = id;
        this.restaurante = restaurante;
    }
}