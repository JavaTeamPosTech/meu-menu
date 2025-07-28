package com.postechfiap.meumenu.core.domain.usecases.restaurante;

import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.core.dtos.restaurante.CadastrarRestauranteInputModel;

public interface CadastrarRestauranteUseCase {
    RestauranteDomain execute(CadastrarRestauranteInputModel input);
}