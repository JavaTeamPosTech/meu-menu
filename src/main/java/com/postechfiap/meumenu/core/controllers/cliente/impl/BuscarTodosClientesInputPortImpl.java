package com.postechfiap.meumenu.core.controllers.cliente.impl;

import com.postechfiap.meumenu.core.controllers.cliente.BuscarTodosClientesInputPort;
import com.postechfiap.meumenu.core.domain.usecases.cliente.BuscarTodosClientesUseCase;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BuscarTodosClientesInputPortImpl implements BuscarTodosClientesInputPort {

    private final BuscarTodosClientesUseCase buscarTodosClientesUseCase;

    @Override
    public void execute() {
        buscarTodosClientesUseCase.execute();
    }
}