package com.postechfiap.meumenu.core.domain.usecases.cliente.impl;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.domain.presenters.cliente.BuscarTodosClientesOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.cliente.BuscarTodosClientesUseCase;
import com.postechfiap.meumenu.core.gateways.ClienteGateway;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class BuscarTodosClientesUseCaseImpl implements BuscarTodosClientesUseCase {

    private final ClienteGateway clienteGateway;
    private final BuscarTodosClientesOutputPort buscarTodosClientesOutputPort;

    @Override
    public List<ClienteDomain> execute() {
        List<ClienteDomain> clientes = clienteGateway.buscarTodosClientes();

        if (clientes.isEmpty()) {
            buscarTodosClientesOutputPort.presentNoContent("Nenhum cliente encontrado.");
            return Collections.emptyList();
        }

        buscarTodosClientesOutputPort.presentSuccess(clientes);
        return clientes;
    }
}