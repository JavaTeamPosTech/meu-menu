package com.postechfiap.meumenu.core.controllers.restaurante.impl;

import com.postechfiap.meumenu.core.controllers.restaurante.BuscarRestaurantePorIdInputPort;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.BuscarRestaurantePorIdUseCase;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class BuscarRestaurantePorIdInputPortImpl implements BuscarRestaurantePorIdInputPort {

    private final BuscarRestaurantePorIdUseCase buscarRestaurantePorIdUseCase;

    @Override
    public void execute(UUID id) {
        buscarRestaurantePorIdUseCase.execute(id);
    }
}