package com.postechfiap.meumenu.core.domain.entities;

import com.postechfiap.meumenu.core.domain.entities.enums.StatusContaEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ProprietarioDomain extends UsuarioDomain {

    private String whatsapp;
    private StatusContaEnum statusConta;

    public ProprietarioDomain(UUID id, String whatsapp, StatusContaEnum statusConta,
                              String nome, String email, String login, String senha,
                              LocalDateTime dataCriacao, LocalDateTime dataAtualizacao, List<EnderecoDomain> enderecos) {
        super(id, nome, email, login, senha, dataCriacao, dataAtualizacao, enderecos);
        this.whatsapp = whatsapp;
        this.statusConta = statusConta;
    }

    public ProprietarioDomain(String nome, String email, String login, String senha,
                              String whatsapp) {
        super(nome, email, login, senha);
        this.whatsapp = whatsapp;
        this.statusConta = StatusContaEnum.ATIVO;
    }
}
