package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.AlterarSenhaInputPort;
import com.postechfiap.meumenu.core.domain.usecases.usuario.AlterarSenhaUseCase;
import com.postechfiap.meumenu.core.dtos.usuario.AlterarSenhaInputModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlterarSenhaInputPortImpl implements AlterarSenhaInputPort {

    private final AlterarSenhaUseCase alterarSenhaUseCase;

    @Override
    public void execute(AlterarSenhaInputModel input) {
        alterarSenhaUseCase.execute(input);
    }
}