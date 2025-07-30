package com.postechfiap.meumenu.infrastructure.model;

import com.postechfiap.meumenu.core.domain.entities.enums.StatusContaEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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

    public ProprietarioEntity(UUID id, String nome, String email, String login, String senha, LocalDateTime dataAtualizacao, LocalDateTime dataCriacao, List<EnderecoEntity> enderecos, String cpf, String whatsapp, StatusContaEnum statusConta) {
        super(id, nome, email, login, senha, dataAtualizacao, dataCriacao, enderecos);
        this.cpf = cpf;
        this.whatsapp = whatsapp;
        this.statusConta = statusConta;
    }
}