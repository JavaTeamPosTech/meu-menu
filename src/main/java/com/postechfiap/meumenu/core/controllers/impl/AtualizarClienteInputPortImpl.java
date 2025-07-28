package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.AtualizarClienteInputPort;
import com.postechfiap.meumenu.core.domain.usecases.cliente.AtualizarClienteUseCase;
import com.postechfiap.meumenu.core.dtos.cliente.AtualizarClienteInputModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AtualizarClienteInputPortImpl implements AtualizarClienteInputPort {

    private final AtualizarClienteUseCase atualizarClienteUseCase;

    @Override
    public void execute(AtualizarClienteInputModel input) {
        atualizarClienteUseCase.execute(input);
    }
}