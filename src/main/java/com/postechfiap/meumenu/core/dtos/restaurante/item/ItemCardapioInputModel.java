package com.postechfiap.meumenu.core.dtos.restaurante.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemCardapioInputModel {
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Boolean disponivelApenasNoRestaurante;
    private String urlFoto;
}