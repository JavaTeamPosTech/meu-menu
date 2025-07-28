package com.postechfiap.meumenu.core.dtos.proprietario;

import com.postechfiap.meumenu.core.domain.entities.enums.StatusContaEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AtualizarProprietarioInputModel {
    private UUID id;
    private String nome;
    private String email;
    private String login;
    private String cpf;
    private String whatsapp;
    private StatusContaEnum statusConta;

}