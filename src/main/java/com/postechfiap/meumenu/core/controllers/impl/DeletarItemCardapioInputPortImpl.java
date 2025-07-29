package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.DeletarItemCardapioInputPort;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.item.DeletarItemCardapioUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeletarItemCardapioInputPortImpl implements DeletarItemCardapioInputPort {

    private final DeletarItemCardapioUseCase deletarItemCardapioUseCase;

    @Override
    public void execute(UUID restauranteId, UUID itemId, UUID proprietarioLogadoId) {
        deletarItemCardapioUseCase.execute(restauranteId, itemId, proprietarioLogadoId);
    }
}