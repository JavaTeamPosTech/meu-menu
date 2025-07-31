package com.postechfiap.meumenu.core.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AdminDomain extends UsuarioDomain {

    public AdminDomain(UUID id, String nome, String email, String login, String senha, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao, List<EnderecoDomain> enderecos) {
        super(id, nome, email, login, senha, dataCriacao, dataAtualizacao, enderecos);
    }
}
