package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.CadastrarProprietarioInputPort;
import com.postechfiap.meumenu.core.domain.usecases.proprietario.CadastrarProprietarioUseCase;
import com.postechfiap.meumenu.core.dtos.proprietario.CadastrarProprietarioInputModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CadastrarProprietarioInputPortImpl implements CadastrarProprietarioInputPort {

    private final CadastrarProprietarioUseCase cadastrarProprietarioUseCase;

    @Override
    public void execute(CadastrarProprietarioInputModel input) {
        cadastrarProprietarioUseCase.execute(input);
    }
}