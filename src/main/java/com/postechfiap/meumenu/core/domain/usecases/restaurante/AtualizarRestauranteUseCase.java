package com.postechfiap.meumenu.core.domain.usecases.restaurante;

import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.core.dtos.restaurante.AtualizarRestauranteInputModel;

import java.util.UUID;

public interface AtualizarRestauranteUseCase {
    RestauranteDomain execute(UUID restauranteId, AtualizarRestauranteInputModel inputModel, UUID proprietarioLogadoId);
}