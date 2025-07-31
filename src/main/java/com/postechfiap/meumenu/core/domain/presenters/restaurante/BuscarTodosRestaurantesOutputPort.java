package com.postechfiap.meumenu.core.domain.presenters.restaurante;

import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;

import java.util.List;

public interface BuscarTodosRestaurantesOutputPort {
    void presentSuccess(List<RestauranteDomain> restaurantes);
    void presentNoContent(String message);
}