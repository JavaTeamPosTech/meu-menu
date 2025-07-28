package com.postechfiap.meumenu.infrastructure.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "itens_cardapio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemCardapioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(name = "disponivel_apenas_no_restaurante", nullable = false)
    private Boolean disponivelApenasNoRestaurante;

    @Column(name = "url_foto")
    private String urlFoto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id", nullable = false)
    private RestauranteEntity restaurante;
}