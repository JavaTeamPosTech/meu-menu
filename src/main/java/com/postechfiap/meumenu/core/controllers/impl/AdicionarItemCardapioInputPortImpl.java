package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.AdicionarItemCardapioInputPort;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.item.AdicionarItemCardapioUseCase;
import com.postechfiap.meumenu.core.dtos.restaurante.item.ItemCardapioInputModel;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class AdicionarItemCardapioInputPortImpl implements AdicionarItemCardapioInputPort {

    private final AdicionarItemCardapioUseCase adicionarItemCardapioUseCase;

    @Override
    public void execute(UUID restauranteId, ItemCardapioInputModel input) {
        adicionarItemCardapioUseCase.execute(restauranteId, input);
    }
}