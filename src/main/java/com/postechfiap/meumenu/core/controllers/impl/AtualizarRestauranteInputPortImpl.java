package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.AtualizarRestauranteInputPort;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.AtualizarRestauranteUseCase;
import com.postechfiap.meumenu.core.dtos.restaurante.AtualizarRestauranteInputModel;
import lombok.RequiredArgsConstructor;
import java.util.UUID;

@RequiredArgsConstructor
public class AtualizarRestauranteInputPortImpl implements AtualizarRestauranteInputPort {

    private final AtualizarRestauranteUseCase atualizarRestauranteUseCase;

    @Override
    public void execute(UUID restauranteId, AtualizarRestauranteInputModel inputModel) {
        atualizarRestauranteUseCase.execute(restauranteId, inputModel);
    }
}