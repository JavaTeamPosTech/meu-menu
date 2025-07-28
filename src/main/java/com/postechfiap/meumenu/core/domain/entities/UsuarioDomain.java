package com.postechfiap.meumenu.core.domain.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioDomain {

    protected UUID id;
    protected String nome;
    protected String email;
    protected String login;
    protected String senha;
    protected LocalDateTime dataAtualizacao;
    protected LocalDateTime dataCriacao;
    protected List<EnderecoDomain> enderecos = new ArrayList<>();

    public UsuarioDomain(String nome, String email, String login, String senha) {
        this.nome = nome;
        this.email = email;
        this.login = login;
        this.senha = senha;
        this.enderecos = new ArrayList<>();
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    public UsuarioDomain(UUID id, String nome, String email, String login, String senha) {
        this(nome, email, login, senha);
        this.id = id;
    }

    public UsuarioDomain(UUID id, String nome, String email, String login, String senha,
                         LocalDateTime dataCriacao, LocalDateTime dataAtualizacao, List<EnderecoDomain> enderecos) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.login = login;
        this.senha = senha;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.enderecos = enderecos != null ? new ArrayList<>(enderecos) : new ArrayList<>();
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public void setEnderecos(List<EnderecoDomain> enderecos) {
        this.enderecos = enderecos != null ? new ArrayList<>(enderecos) : new ArrayList<>();
    }
}