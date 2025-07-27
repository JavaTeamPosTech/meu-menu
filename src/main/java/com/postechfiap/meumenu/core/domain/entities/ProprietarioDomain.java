package com.postechfiap.meumenu.core.domain.entities;

import com.postechfiap.meumenu.core.domain.entities.enums.StatusContaEnum;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class ProprietarioDomain extends UsuarioDomain {

    private String whatsapp;

    private StatusContaEnum statusConta;

    private List<EnderecoDomain> enderecos;

    public ProprietarioDomain(UUID id, String cnpj, String razaoSocial, String nomeFantasia, String inscricaoEstadual,
                              String telefoneComercial, String whatsapp, StatusContaEnum statusConta, String nome,
                              String email, String login, String senha) {
        super(id, nome, email, login, senha);
        this.whatsapp = whatsapp;
        this.statusConta = statusConta;
    }
}
