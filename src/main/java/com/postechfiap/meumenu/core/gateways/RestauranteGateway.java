package com.postechfiap.meumenu.core.gateways;

import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;

import java.util.List;

public interface RestauranteGateway {
    RestauranteDomain cadastrarRestaurante(RestauranteDomain restauranteDomain);
    boolean existsByCnpj(String cnpj);
    List<RestauranteDomain> buscarTodosRestaurantes();
}