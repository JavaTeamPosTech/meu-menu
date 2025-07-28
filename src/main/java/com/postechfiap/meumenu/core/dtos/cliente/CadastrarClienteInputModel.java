package com.postechfiap.meumenu.core.dtos.cliente;

import com.postechfiap.meumenu.core.domain.entities.enums.AlergiaAlimentarEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.GeneroEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.MetodoPagamentoEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.TiposComidaEnum;
import com.postechfiap.meumenu.core.dtos.endereco.EnderecoInputModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CadastrarClienteInputModel {

    private String nome;
    private String email;
    private String login;
    private String senha;
    private String cpf;
    private LocalDate dataNascimento;
    private GeneroEnum genero;
    private String telefone;
    private Set<TiposComidaEnum> preferenciasAlimentares;
    private Set<AlergiaAlimentarEnum> alergias;
    private MetodoPagamentoEnum metodoPagamentoPreferido;
    private Boolean notificacoesAtivas;
    private List<EnderecoInputModel> enderecos;

}