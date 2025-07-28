package com.postechfiap.meumenu.core.domain.usecases.usuario;

import com.postechfiap.meumenu.core.dtos.usuario.AlterarSenhaInputModel;

public interface AlterarSenhaUseCase {
    void execute(AlterarSenhaInputModel input);
}