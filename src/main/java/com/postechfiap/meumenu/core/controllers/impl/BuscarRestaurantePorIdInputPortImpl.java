package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.BuscarRestaurantePorIdInputPort;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.BuscarRestaurantePorIdUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BuscarRestaurantePorIdInputPortImpl implements BuscarRestaurantePorIdInputPort {

    private final BuscarRestaurantePorIdUseCase buscarRestaurantePorIdUseCase;

    @Override
    public void execute(UUID id) {
        buscarRestaurantePorIdUseCase.execute(id);
    }
}