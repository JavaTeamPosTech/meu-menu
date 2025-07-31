package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.CadastrarClienteInputPort;
import com.postechfiap.meumenu.core.domain.usecases.cliente.CadastrarClienteUseCase;
import com.postechfiap.meumenu.core.dtos.cliente.CadastrarClienteInputModel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CadastrarClienteInputPortImpl implements CadastrarClienteInputPort {

    private final CadastrarClienteUseCase cadastrarClienteUseCase;

    @Override
    public void execute(CadastrarClienteInputModel input) {
        cadastrarClienteUseCase.execute(input);
    }
}