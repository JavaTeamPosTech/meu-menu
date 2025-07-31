package com.postechfiap.meumenu.core.controllers.restaurante.item.impl;

import com.postechfiap.meumenu.core.controllers.restaurante.item.DeletarItemCardapioInputPort;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.item.DeletarItemCardapioUseCase;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class DeletarItemCardapioInputPortImpl implements DeletarItemCardapioInputPort {

    private final DeletarItemCardapioUseCase deletarItemCardapioUseCase;

    @Override
    public void execute(UUID restauranteId, UUID itemId, UUID proprietarioLogadoId) {
        deletarItemCardapioUseCase.execute(restauranteId, itemId, proprietarioLogadoId);
    }
}