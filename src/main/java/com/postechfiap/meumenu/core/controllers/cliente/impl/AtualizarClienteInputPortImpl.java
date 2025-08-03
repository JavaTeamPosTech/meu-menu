package com.postechfiap.meumenu.core.controllers.cliente.impl;

import com.postechfiap.meumenu.core.controllers.cliente.AtualizarClienteInputPort;
import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.domain.presenters.cliente.AtualizarClienteOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.cliente.AtualizarClienteUseCase;
import com.postechfiap.meumenu.core.dtos.cliente.AtualizarClienteInputModel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AtualizarClienteInputPortImpl implements AtualizarClienteInputPort {

    private final AtualizarClienteUseCase atualizarClienteUseCase;

    private final AtualizarClienteOutputPort atualizarClienteOutputPort;

    @Override
    public void execute(AtualizarClienteInputModel input) {
        try {
            ClienteDomain clienteAtualizadoDomain = atualizarClienteUseCase.execute(input);
            atualizarClienteOutputPort.presentSuccess(clienteAtualizadoDomain);
        } catch (Exception e) {
            atualizarClienteOutputPort.presentError(e.getMessage());
        }
    }
}