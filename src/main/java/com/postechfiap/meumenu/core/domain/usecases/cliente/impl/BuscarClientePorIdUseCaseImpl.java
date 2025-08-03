package com.postechfiap.meumenu.core.domain.usecases.cliente.impl;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.domain.usecases.cliente.BuscarClientePorIdUseCase;
import com.postechfiap.meumenu.core.gateways.ClienteGateway;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class BuscarClientePorIdUseCaseImpl implements BuscarClientePorIdUseCase {

    private final ClienteGateway clienteGateway;


    @Override
    public Optional<ClienteDomain> execute(UUID id) {
        return clienteGateway.buscarClientePorId(id);
    }

}