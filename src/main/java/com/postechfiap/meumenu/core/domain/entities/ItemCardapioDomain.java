package com.postechfiap.meumenu.core.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ItemCardapioDomain {

    private UUID id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Boolean disponivelApenasNoRestaurante;
    private String urlFoto;
    private RestauranteDomain restaurante;

    public ItemCardapioDomain(String nome, String descricao, BigDecimal preco,
                              Boolean disponivelApenasNoRestaurante, String urlFoto) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.disponivelApenasNoRestaurante = disponivelApenasNoRestaurante;
        this.urlFoto = urlFoto;
    }

    public ItemCardapioDomain(UUID id, String nome, String descricao, BigDecimal preco,
                              Boolean disponivelApenasNoRestaurante, String urlFoto, RestauranteDomain restaurante) {
        this(nome, descricao, preco, disponivelApenasNoRestaurante, urlFoto);
        this.id = id;
        this.restaurante = restaurante;
    }
}