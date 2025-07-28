package com.postechfiap.meumenu.core.dtos.proprietario;

import com.postechfiap.meumenu.core.dtos.endereco.EnderecoInputModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CadastrarProprietarioInputModel {

    private String nome;
    private String email;
    private String login;
    private String senha;
    private String cpf;
    private String whatsapp;
    private List<EnderecoInputModel> enderecos;
}