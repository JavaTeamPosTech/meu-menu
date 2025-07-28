package com.postechfiap.meumenu.core.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class EnderecoRestauranteDomain {

    private UUID id;
    private String estado;
    private String cidade;
    private String bairro;
    private String rua;
    private Integer numero;
    private String complemento;
    private String cep;
    private RestauranteDomain restaurante;

    public EnderecoRestauranteDomain(String estado, String cidade, String bairro, String rua, Integer numero,
                                     String complemento, String cep) {
        this.id = UUID.randomUUID();
        this.estado = estado;
        this.cidade = cidade;
        this.bairro = bairro;
        this.rua = rua;
        this.numero = numero;
        this.complemento = complemento;
        this.cep = cep;
    }

    public EnderecoRestauranteDomain(UUID id, String estado, String cidade, String bairro, String rua, Integer numero,
                                     String complemento, String cep, RestauranteDomain restaurante) {
        this(estado, cidade, bairro, rua, numero, complemento, cep);
        this.id = id;
        this.restaurante = restaurante;
    }
}