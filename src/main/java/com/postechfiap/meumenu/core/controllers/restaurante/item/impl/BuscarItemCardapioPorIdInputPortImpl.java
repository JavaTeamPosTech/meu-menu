package com.postechfiap.meumenu.core.controllers.restaurante.item.impl;

import com.postechfiap.meumenu.core.controllers.restaurante.item.BuscarItemCardapioPorIdInputPort;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.item.BuscarItemCardapioPorIdUseCase;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class BuscarItemCardapioPorIdInputPortImpl implements BuscarItemCardapioPorIdInputPort {

    private final BuscarItemCardapioPorIdUseCase buscarItemCardapioPorIdUseCase;

    @Override
    public void execute(UUID restauranteId, UUID itemId) {
        buscarItemCardapioPorIdUseCase.execute(restauranteId, itemId);
    }
}