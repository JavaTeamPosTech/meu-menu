package com.postechfiap.meumenu.core.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class RestauranteDomain {

    private UUID id;
    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;
    private String inscricaoEstadual;
    private String telefoneComercial;

    private ProprietarioDomain proprietario;

    private EnderecoRestauranteDomain endereco;
    private List<TipoCozinhaDomain> tiposCozinha = new ArrayList<>();
    private List<HorarioFuncionamentoDomain> horariosFuncionamento = new ArrayList<>();
    private List<ItemCardapioDomain> itensCardapio = new ArrayList<>();

    public RestauranteDomain(String cnpj, String razaoSocial, String nomeFantasia, String inscricaoEstadual,
                             String telefoneComercial, ProprietarioDomain proprietario,
                             EnderecoRestauranteDomain endereco, List<TipoCozinhaDomain> tiposCozinha,
                             List<HorarioFuncionamentoDomain> horariosFuncionamento) {
        this.id = UUID.randomUUID();
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.nomeFantasia = nomeFantasia;
        this.inscricaoEstadual = inscricaoEstadual;
        this.telefoneComercial = telefoneComercial;
        this.proprietario = proprietario;
        this.endereco = endereco;
        this.tiposCozinha = tiposCozinha != null ? new ArrayList<>(tiposCozinha) : new ArrayList<>();
        this.horariosFuncionamento = horariosFuncionamento != null ? new ArrayList<>(horariosFuncionamento) : new ArrayList<>();
        this.itensCardapio = new ArrayList<>();
    }

    public RestauranteDomain(UUID id, String cnpj, String razaoSocial, String nomeFantasia, String inscricaoEstadual,
                             String telefoneComercial, ProprietarioDomain proprietario, EnderecoRestauranteDomain endereco,
                             List<TipoCozinhaDomain> tiposCozinha, List<HorarioFuncionamentoDomain> horariosFuncionamento,
                             List<ItemCardapioDomain> itensCardapio) {
        this(cnpj, razaoSocial, nomeFantasia, inscricaoEstadual, telefoneComercial, proprietario, endereco,
                tiposCozinha, horariosFuncionamento);
        this.id = id;
        this.itensCardapio = itensCardapio != null ? new ArrayList<>(itensCardapio) : new ArrayList<>();
    }

}