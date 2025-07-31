package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.BuscarClientePorIdInputPort;
import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.domain.usecases.cliente.BuscarClientePorIdUseCase;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class BuscarClientePorIdInputPortImpl implements BuscarClientePorIdInputPort {

    private final BuscarClientePorIdUseCase buscarClientePorIdUseCase;

    @Override
    public Optional<ClienteDomain> execute(UUID id) {
        return buscarClientePorIdUseCase.execute(id);
    }
}