package com.postechfiap.meumenu.core.domain.entities;

import com.postechfiap.meumenu.core.domain.entities.enums.StatusContaEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ProprietarioDomain extends UsuarioDomain {

    private String cpf;
    private String whatsapp;
    private StatusContaEnum statusConta;
    private List<RestauranteDomain> restaurantes = new ArrayList<>();

    public ProprietarioDomain(UUID id, String cpf, String whatsapp, StatusContaEnum statusConta,
                              String nome, String email, String login, String senha,
                              LocalDateTime dataCriacao, LocalDateTime dataAtualizacao, List<EnderecoDomain> enderecos, List<RestauranteDomain> restaurantes) {
        super(id, nome, email, login, senha, dataCriacao, dataAtualizacao, enderecos);
        this.cpf = cpf;
        this.whatsapp = whatsapp;
        this.statusConta = statusConta;
        this.restaurantes = restaurantes != null ? new ArrayList<>(restaurantes) : new ArrayList<>();
    }

    public ProprietarioDomain(String nome, String email, String login, String senha,
                              String cpf, String whatsapp) {
        super(nome, email, login, senha);
        this.cpf = cpf;
        this.whatsapp = whatsapp;
        this.statusConta = StatusContaEnum.ATIVO;
        this.restaurantes = new ArrayList<>();
    }
}
