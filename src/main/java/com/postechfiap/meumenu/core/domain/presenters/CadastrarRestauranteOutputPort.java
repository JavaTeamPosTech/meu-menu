package com.postechfiap.meumenu.core.domain.presenters;

import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;

public interface CadastrarRestauranteOutputPort {
    void presentSuccess(RestauranteDomain restaurante);
    void presentError(String message);
}