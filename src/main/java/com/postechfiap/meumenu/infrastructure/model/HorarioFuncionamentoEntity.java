package com.postechfiap.meumenu.infrastructure.model;

import com.postechfiap.meumenu.core.domain.entities.enums.DiaSemanaEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "horarios_funcionamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HorarioFuncionamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private LocalTime abre;

    @Column(nullable = false)
    private LocalTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana", nullable = false)
    private DiaSemanaEnum diaSemana;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id", nullable = false)
    private RestauranteEntity restaurante;
}