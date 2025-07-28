package com.postechfiap.meumenu.core.controllers;

import com.postechfiap.meumenu.core.dtos.usuario.AlterarSenhaInputModel;

public interface AlterarSenhaInputPort {
    void execute(AlterarSenhaInputModel input);
}