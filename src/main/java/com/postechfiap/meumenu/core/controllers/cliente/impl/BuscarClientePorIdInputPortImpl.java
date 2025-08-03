package com.postechfiap.meumenu.core.controllers.cliente.impl;

import com.postechfiap.meumenu.core.controllers.cliente.BuscarClientePorIdInputPort;
import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.domain.presenters.cliente.BuscarClienteOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.cliente.BuscarClientePorIdUseCase;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class BuscarClientePorIdInputPortImpl implements BuscarClientePorIdInputPort {

    private final BuscarClientePorIdUseCase buscarClientePorIdUseCase;

    private final BuscarClienteOutputPort buscarClienteOutputPort;

    @Override
    public void execute(UUID id) {
        Optional<ClienteDomain> clienteOptional = buscarClientePorIdUseCase.execute(id);
        if (clienteOptional.isEmpty()) {
            buscarClienteOutputPort.presentNoContent("Cliente não encontrado.");
        } else {
            buscarClienteOutputPort.presentSuccess(clienteOptional.get());
        }

    }
}