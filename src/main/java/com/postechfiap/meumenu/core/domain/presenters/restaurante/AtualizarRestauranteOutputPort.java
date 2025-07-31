package com.postechfiap.meumenu.core.domain.presenters.restaurante;

import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;

public interface AtualizarRestauranteOutputPort {
    void presentSuccess(RestauranteDomain restaurante);
}