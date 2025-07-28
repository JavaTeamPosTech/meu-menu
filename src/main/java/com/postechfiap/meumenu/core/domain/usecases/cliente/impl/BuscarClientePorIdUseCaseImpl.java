package com.postechfiap.meumenu.core.domain.usecases.cliente.impl;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.domain.presenters.BuscarClienteOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.cliente.BuscarClientePorIdUseCase;
import com.postechfiap.meumenu.core.exceptions.BusinessException;
import com.postechfiap.meumenu.core.gateways.ClienteGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BuscarClientePorIdUseCaseImpl implements BuscarClientePorIdUseCase {

    private final ClienteGateway clienteGateway;
    private final BuscarClienteOutputPort buscarClienteOutputPort;

    @Override
    public Optional<ClienteDomain> execute(UUID id) {
        Optional<ClienteDomain> clienteOptional = clienteGateway.buscarClientePorId(id);

        if (clienteOptional.isEmpty()) {
            throw new BusinessException("Cliente com ID " + id + " não encontrado.");
        }
        buscarClienteOutputPort.presentSuccess(clienteOptional.get());
        return clienteOptional;
    }

}