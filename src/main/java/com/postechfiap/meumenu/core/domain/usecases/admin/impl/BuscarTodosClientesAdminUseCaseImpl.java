package com.postechfiap.meumenu.core.domain.usecases.admin.impl;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.domain.presenters.cliente.BuscarTodosClientesOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.admin.BuscarTodosClientesAdminUseCase;
import com.postechfiap.meumenu.core.gateways.ClienteGateway;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class BuscarTodosClientesAdminUseCaseImpl implements BuscarTodosClientesAdminUseCase {

    private final ClienteGateway clienteGateway;


    @Override
    public List<ClienteDomain> execute() {
        return clienteGateway.buscarTodosClientes();
    }
}