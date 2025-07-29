package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.AtualizarRestauranteInputPort;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.AtualizarRestauranteUseCase;
import com.postechfiap.meumenu.core.dtos.restaurante.AtualizarRestauranteInputModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AtualizarRestauranteInputPortImpl implements AtualizarRestauranteInputPort {

    private final AtualizarRestauranteUseCase atualizarRestauranteUseCase;

    @Override
    public void execute(UUID restauranteId, AtualizarRestauranteInputModel inputModel) {
        atualizarRestauranteUseCase.execute(restauranteId, inputModel);
    }
}