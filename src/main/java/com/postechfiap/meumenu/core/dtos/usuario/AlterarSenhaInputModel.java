package com.postechfiap.meumenu.core.dtos.usuario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlterarSenhaInputModel {
    private UUID usuarioId;
    private String senhaAntiga;
    private String novaSenha;
}