package com.postechfiap.meumenu.core.controllers.restaurante.impl;

import com.postechfiap.meumenu.core.controllers.restaurante.BuscarTodosRestaurantesInputPort;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.BuscarTodosRestaurantesUseCase;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BuscarTodosRestaurantesInputPortImpl implements BuscarTodosRestaurantesInputPort {

    private final BuscarTodosRestaurantesUseCase buscarTodosRestaurantesUseCase;

    @Override
    public void execute() {
        buscarTodosRestaurantesUseCase.execute();
    }
}