package com.postechfiap.meumenu.core.controllers.cliente.impl;

import com.postechfiap.meumenu.core.controllers.cliente.DeletarClienteInputPort;
import com.postechfiap.meumenu.core.domain.presenters.cliente.DeletarClienteOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.cliente.DeletarClienteUseCase;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class DeletarClienteInputPortImpl implements DeletarClienteInputPort {

    private final DeletarClienteUseCase deletarClienteUseCase;

    private final DeletarClienteOutputPort deletarClienteOutputPort;

    @Override
    public void execute(UUID id) {
        try {
            deletarClienteUseCase.execute(id);
            deletarClienteOutputPort.presentSuccess("Cliente com ID " + id + " excluído com sucesso.");
        } catch (Exception e) {
            deletarClienteOutputPort.presentError(e.getMessage());
        }

    }
}
