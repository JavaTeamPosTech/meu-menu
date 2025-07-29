package com.postechfiap.meumenu.core.domain.usecases.restaurante.item;

import com.postechfiap.meumenu.core.domain.entities.ItemCardapioDomain;
import com.postechfiap.meumenu.core.dtos.restaurante.item.ItemCardapioInputModel;

import java.util.UUID;

public interface AdicionarItemCardapioUseCase {
    ItemCardapioDomain execute(UUID restauranteId, ItemCardapioInputModel input);
}