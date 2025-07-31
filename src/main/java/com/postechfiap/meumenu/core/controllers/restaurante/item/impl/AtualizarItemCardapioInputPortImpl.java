package com.postechfiap.meumenu.core.controllers.restaurante.item.impl;

import com.postechfiap.meumenu.core.controllers.restaurante.item.AtualizarItemCardapioInputPort;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.item.AtualizarItemCardapioUseCase;
import com.postechfiap.meumenu.core.dtos.restaurante.item.ItemCardapioInputModel;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class AtualizarItemCardapioInputPortImpl implements AtualizarItemCardapioInputPort {

    private final AtualizarItemCardapioUseCase atualizarItemCardapioUseCase;

    @Override
    public void execute(UUID restauranteId, UUID itemId, ItemCardapioInputModel inputModel, UUID proprietarioLogadoId) {
        atualizarItemCardapioUseCase.execute(restauranteId, itemId, inputModel, proprietarioLogadoId);
    }
}