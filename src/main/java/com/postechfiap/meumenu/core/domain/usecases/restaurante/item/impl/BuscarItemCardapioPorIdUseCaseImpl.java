package com.postechfiap.meumenu.core.domain.usecases.restaurante.item.impl;

import com.postechfiap.meumenu.core.domain.entities.ItemCardapioDomain;
import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.core.domain.presenters.BuscarItemCardapioPorIdOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.item.BuscarItemCardapioPorIdUseCase;
import com.postechfiap.meumenu.core.exceptions.ResourceNotFoundException;
import com.postechfiap.meumenu.core.gateways.RestauranteGateway;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class BuscarItemCardapioPorIdUseCaseImpl implements BuscarItemCardapioPorIdUseCase {

    private final RestauranteGateway restauranteGateway;
    private final BuscarItemCardapioPorIdOutputPort buscarItemCardapioPorIdOutputPort;

    @Override
    public ItemCardapioDomain execute(UUID restauranteId, UUID itemId) {
        Optional<RestauranteDomain> restauranteOptional = restauranteGateway.buscarRestaurantePorId(restauranteId);
        if (restauranteOptional.isEmpty()) {
            throw new ResourceNotFoundException("Restaurante com ID " + restauranteId + " não encontrado.");
        }
        RestauranteDomain restauranteExistente = restauranteOptional.get();

        Optional<ItemCardapioDomain> itemOptional = restauranteExistente.getItensCardapio().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst();

        if (itemOptional.isEmpty()) {
            throw new ResourceNotFoundException("Item do cardápio com ID " + itemId + " não encontrado no restaurante ID " + restauranteId + ".");
        }
        ItemCardapioDomain itemEncontrado = itemOptional.get();

        buscarItemCardapioPorIdOutputPort.presentSuccess(itemEncontrado);

        return itemEncontrado;
    }
}