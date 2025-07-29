package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.DeletarRestauranteInputPort;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.DeletarRestauranteUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeletarRestauranteInputPortImpl implements DeletarRestauranteInputPort {

    private final DeletarRestauranteUseCase deletarRestauranteUseCase;

    @Override
    public void execute(UUID id, UUID proprietarioLogadoId) {
        deletarRestauranteUseCase.execute(id, proprietarioLogadoId);
    }
}