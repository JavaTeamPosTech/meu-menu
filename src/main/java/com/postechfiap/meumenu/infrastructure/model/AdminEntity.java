package com.postechfiap.meumenu.infrastructure.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "admins")
@NoArgsConstructor
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "id")
public class AdminEntity extends UsuarioEntity {

    public AdminEntity(UUID id, String nome, String email, String login, String senha,
                       LocalDateTime dataCriacao, LocalDateTime dataAtualizacao, List<EnderecoEntity> enderecos) {
        super(id, nome, email, login, senha, dataCriacao, dataAtualizacao, enderecos);
    }

    public AdminEntity(String nome, String email, String login, String senha, List<EnderecoEntity> enderecos) {
        super(nome, email, login, senha, enderecos);
    }
}