package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.DeletarClienteInputPort;
import com.postechfiap.meumenu.core.domain.usecases.cliente.DeletarClienteUseCase;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class DeletarClienteInputPortImpl implements DeletarClienteInputPort {

    private final DeletarClienteUseCase deletarClienteUseCase;

    @Override
    public void execute(UUID id) {
        deletarClienteUseCase.execute(id);
    }
}
