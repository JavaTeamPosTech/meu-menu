package com.postechfiap.meumenu.core.controllers.cliente.impl;

import com.postechfiap.meumenu.core.controllers.cliente.CadastrarClienteInputPort;
import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.domain.presenters.cliente.CadastrarClienteOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.cliente.CadastrarClienteUseCase;
import com.postechfiap.meumenu.core.dtos.cliente.CadastrarClienteInputModel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CadastrarClienteInputPortImpl implements CadastrarClienteInputPort {

    private final CadastrarClienteUseCase cadastrarClienteUseCase;

    private final CadastrarClienteOutputPort clienteOutputPort;

    @Override
    public void execute(CadastrarClienteInputModel input) {
        try {
            ClienteDomain clienteSalvo = cadastrarClienteUseCase.execute(input);
            clienteOutputPort.presentSuccess(clienteSalvo);
        } catch (Exception e) {
            clienteOutputPort.presentError(e.getMessage());
        }

    }
}