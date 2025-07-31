package com.postechfiap.meumenu.core.domain.usecases.cliente.impl;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.domain.presenters.DeletarClienteOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.cliente.DeletarClienteUseCase;
import com.postechfiap.meumenu.core.gateways.ClienteGateway;
import com.postechfiap.meumenu.core.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class DeletarClienteUseCaseImpl implements DeletarClienteUseCase {

    private final ClienteGateway clienteGateway;
    private final DeletarClienteOutputPort deletarClienteOutputPort;

    @Override
    public void execute(UUID id) {
        Optional<ClienteDomain> clienteOptional = clienteGateway.buscarClientePorId(id);

        if (clienteOptional.isEmpty()) {
            throw new ResourceNotFoundException("Cliente com ID " + id + " não encontrado para exclusão.");
        }

        clienteGateway.deletarCliente(id);
        deletarClienteOutputPort.presentSuccess("Cliente com ID " + id + " excluído com sucesso.");
    }
}