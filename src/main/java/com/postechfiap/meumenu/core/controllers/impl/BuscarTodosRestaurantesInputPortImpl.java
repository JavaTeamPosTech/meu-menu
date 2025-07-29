package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.BuscarTodosRestaurantesInputPort;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.BuscarTodosRestaurantesUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BuscarTodosRestaurantesInputPortImpl implements BuscarTodosRestaurantesInputPort {

    private final BuscarTodosRestaurantesUseCase buscarTodosRestaurantesUseCase;

    @Override
    public void execute() {
        buscarTodosRestaurantesUseCase.execute();
    }
}