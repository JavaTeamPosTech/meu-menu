package com.postechfiap.meumenu.core.domain.usecases.restaurante.impl;

import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.core.domain.presenters.BuscarTodosRestaurantesOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.BuscarTodosRestaurantesUseCase;
import com.postechfiap.meumenu.core.gateways.RestauranteGateway;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class BuscarTodosRestaurantesUseCaseImpl implements BuscarTodosRestaurantesUseCase {

    private final RestauranteGateway restauranteGateway;
    private final BuscarTodosRestaurantesOutputPort buscarTodosRestaurantesOutputPort;

    @Override
    public List<RestauranteDomain> execute() {
        List<RestauranteDomain> restaurantes = restauranteGateway.buscarTodosRestaurantes();

        if (restaurantes.isEmpty()) {
            buscarTodosRestaurantesOutputPort.presentNoContent("Nenhum restaurante encontrado.");
            return Collections.emptyList();
        }

        buscarTodosRestaurantesOutputPort.presentSuccess(restaurantes);
        return restaurantes;
    }
}