package com.postechfiap.meumenu.core.controllers.cliente.impl;

import com.postechfiap.meumenu.core.controllers.cliente.BuscarTodosClientesInputPort;
import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.domain.presenters.cliente.BuscarTodosClientesOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.cliente.BuscarTodosClientesUseCase;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BuscarTodosClientesInputPortImpl implements BuscarTodosClientesInputPort {

    private final BuscarTodosClientesUseCase buscarTodosClientesUseCase;

    private final BuscarTodosClientesOutputPort buscarTodosClientesOutputPort;


    @Override
    public void execute() {

        List<ClienteDomain> clientes = buscarTodosClientesUseCase.execute();

        if (clientes.isEmpty()) {
            buscarTodosClientesOutputPort.presentNoContent("Nenhum cliente encontrado.");

        } else {
            buscarTodosClientesOutputPort.presentSuccess(clientes);
        }

    }
}