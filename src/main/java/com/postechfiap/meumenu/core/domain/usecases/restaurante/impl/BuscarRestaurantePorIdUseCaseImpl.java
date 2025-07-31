package com.postechfiap.meumenu.core.domain.usecases.restaurante.impl;

import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.core.domain.presenters.BuscarRestaurantePorIdOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.BuscarRestaurantePorIdUseCase;
import com.postechfiap.meumenu.core.exceptions.ResourceNotFoundException;
import com.postechfiap.meumenu.core.gateways.RestauranteGateway;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class BuscarRestaurantePorIdUseCaseImpl implements BuscarRestaurantePorIdUseCase {

    private final RestauranteGateway restauranteGateway;
    private final BuscarRestaurantePorIdOutputPort buscarRestaurantePorIdOutputPort;

    @Override
    public RestauranteDomain execute(UUID id) {
        Optional<RestauranteDomain> restauranteOptional = restauranteGateway.buscarRestaurantePorId(id);

        if (restauranteOptional.isEmpty()) {
            throw new ResourceNotFoundException("Restaurante com ID " + id + " não encontrado.");
        }

        RestauranteDomain restauranteEncontrado = restauranteOptional.get();
        buscarRestaurantePorIdOutputPort.presentSuccess(restauranteEncontrado);
        return restauranteEncontrado;
    }
}