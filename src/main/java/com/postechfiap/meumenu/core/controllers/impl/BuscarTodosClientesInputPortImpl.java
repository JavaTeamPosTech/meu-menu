package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.BuscarTodosClientesInputPort;
import com.postechfiap.meumenu.core.domain.usecases.cliente.BuscarTodosClientesUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BuscarTodosClientesInputPortImpl implements BuscarTodosClientesInputPort {

    private final BuscarTodosClientesUseCase buscarTodosClientesUseCase;

    @Override
    public void execute() {
        buscarTodosClientesUseCase.execute();
    }
}