package com.postechfiap.meumenu.infrastructure.model;

import com.postechfiap.meumenu.core.domain.entities.enums.AlergiaAlimentarEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.GeneroEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.MetodoPagamentoEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.TiposComidaEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "clientes",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_cliente_cpf", columnNames = "cpf"),
                @UniqueConstraint(name = "uk_cliente_telefone", columnNames = "telefone")
        })
@PrimaryKeyJoinColumn(name = "id")
public class ClienteEntity extends UsuarioEntity {

    private String cpf;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    private GeneroEnum genero;

    private String telefone;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "cliente_preferencias_alimentares", joinColumns = @JoinColumn(name = "cliente_id"))
    @Column(name = "preferencia_alimentar", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<TiposComidaEnum> preferenciasAlimentares = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "cliente_alergias", joinColumns = @JoinColumn(name = "cliente_id"))
    @Column(name = "alergia", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<AlergiaAlimentarEnum> alergias = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pagamento_preferido")
    private MetodoPagamentoEnum metodoPagamentoPreferido;

    private LocalDateTime ultimoPedido;

    @Column(name = "saldo_pontos")
    private Integer saldoPontos;

    @Column(name = "cliente_vip")
    private Boolean clienteVip;

    @Column(name = "avaliacoes_feitas")
    private Integer avaliacoesFeitas;

    @Column(name = "notificacoes_ativas")
    private Boolean notificacoesAtivas;

}