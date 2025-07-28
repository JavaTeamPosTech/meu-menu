package com.postechfiap.meumenu.core.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoCozinhaDomain {

    private UUID id;
    private String nome;

    public TipoCozinhaDomain(String nome) {
        this.id = UUID.randomUUID();
        this.nome = nome;
    }
}