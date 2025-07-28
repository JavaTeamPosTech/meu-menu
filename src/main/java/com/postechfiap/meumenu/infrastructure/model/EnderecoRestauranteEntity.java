package com.postechfiap.meumenu.infrastructure.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "enderecos_restaurante")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoRestauranteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private String cidade;

    @Column(nullable = false)
    private String bairro;

    @Column(nullable = false)
    private String rua;

    @Column(nullable = false)
    private Integer numero;

    private String complemento; // Pode ser nulo

    @Column(nullable = false)
    private String cep;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id", unique = true, nullable = false)
    private RestauranteEntity restaurante;

}