package com.postechfiap.meumenu.core.dtos.restaurante;

import com.postechfiap.meumenu.core.dtos.restaurante.endereco.EnderecoRestauranteInputModel;
import com.postechfiap.meumenu.core.dtos.restaurante.horario.HorarioFuncionamentoInputModel;
import com.postechfiap.meumenu.core.dtos.restaurante.tipocozinha.TipoCozinhaInputModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CadastrarRestauranteInputModel {

    private UUID proprietarioId;
    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;
    private String inscricaoEstadual;
    private String telefoneComercial;

    private EnderecoRestauranteInputModel endereco;
    private List<TipoCozinhaInputModel> tiposCozinha;
    private List<HorarioFuncionamentoInputModel> horariosFuncionamento;
}