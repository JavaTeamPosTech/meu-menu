package com.postechfiap.meumenu.core.gateways;

import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;

public interface RestauranteGateway {
    RestauranteDomain cadastrarRestaurante(RestauranteDomain restauranteDomain);
    boolean existsByCnpj(String cnpj);
}