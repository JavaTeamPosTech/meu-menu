package com.postechfiap.meumenu.core.domain.usecases.restaurante;

import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;

import java.util.List;

public interface BuscarTodosRestaurantesUseCase {
    List<RestauranteDomain> execute();
}