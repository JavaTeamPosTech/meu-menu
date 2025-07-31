package com.postechfiap.meumenu.infrastructure.model;

import com.postechfiap.meumenu.core.domain.entities.enums.StatusContaEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
@Getter
@Table(name = "proprietarios")
@PrimaryKeyJoinColumn(name = "id")
public class ProprietarioEntity extends UsuarioEntity {

    private String cpf;
    private String whatsapp;

    @Column(name = "status_conta")
    @Enumerated(EnumType.STRING)
    private StatusContaEnum statusConta;

    @OneToMany(mappedBy = "proprietario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RestauranteEntity> restaurantes = new ArrayList<>();

}