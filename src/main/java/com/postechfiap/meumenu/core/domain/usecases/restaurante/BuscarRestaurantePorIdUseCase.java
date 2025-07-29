package com.postechfiap.meumenu.core.domain.usecases.restaurante;

import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;

import java.util.UUID;

public interface BuscarRestaurantePorIdUseCase {
    RestauranteDomain execute(UUID id);
}