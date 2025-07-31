package com.postechfiap.meumenu.core.controllers.proprietario.impl;

import com.postechfiap.meumenu.core.controllers.proprietario.CadastrarProprietarioInputPort;
import com.postechfiap.meumenu.core.domain.usecases.proprietario.CadastrarProprietarioUseCase;
import com.postechfiap.meumenu.core.dtos.proprietario.CadastrarProprietarioInputModel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CadastrarProprietarioInputPortImpl implements CadastrarProprietarioInputPort {

    private final CadastrarProprietarioUseCase cadastrarProprietarioUseCase;

    @Override
    public void execute(CadastrarProprietarioInputModel input) {
        cadastrarProprietarioUseCase.execute(input);
    }
}