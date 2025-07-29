package com.postechfiap.meumenu.core.gateways;

import com.postechfiap.meumenu.core.domain.entities.ItemCardapioDomain;
import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestauranteGateway {
    RestauranteDomain cadastrarRestaurante(RestauranteDomain restauranteDomain);
    boolean existsByCnpj(String cnpj);
    List<RestauranteDomain> buscarTodosRestaurantes();
    Optional<RestauranteDomain> buscarRestaurantePorId(UUID id);
    RestauranteDomain atualizarRestaurante(RestauranteDomain restauranteDomain);
    ItemCardapioDomain adicionarItemCardapio(UUID restauranteId, ItemCardapioDomain itemCardapioDomain);
    void deletarRestaurante(UUID id);
}