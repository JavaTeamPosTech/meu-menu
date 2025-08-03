package com.postechfiap.meumenu.core.domain.usecases.cliente.impl;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.domain.usecases.cliente.BuscarTodosClientesUseCase;
import com.postechfiap.meumenu.core.gateways.ClienteGateway;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BuscarTodosClientesUseCaseImpl implements BuscarTodosClientesUseCase {

    private final ClienteGateway clienteGateway;

    @Override
    public List<ClienteDomain> execute() {
        return clienteGateway.buscarTodosClientes();
    }
}