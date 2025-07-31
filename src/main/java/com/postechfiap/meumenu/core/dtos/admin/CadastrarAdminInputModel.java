package com.postechfiap.meumenu.core.dtos.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CadastrarAdminInputModel {
    private String nome;
    private String email;
    private String login;
    private String senha;
}


