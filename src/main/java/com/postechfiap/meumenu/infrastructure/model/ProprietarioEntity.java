package com.postechfiap.meumenu.infrastructure.model;

import com.postechfiap.meumenu.core.domain.entities.enums.StatusContaEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
@Getter
@Table(name = "proprietarios")
@PrimaryKeyJoinColumn(name = "id")
public class ProprietarioEntity extends UsuarioEntity {

    private String whatsapp;

    @Column(name = "status_conta")
    @Enumerated(EnumType.STRING)
    private StatusContaEnum statusConta;

}