package com.postechfiap.meumenu.core.domain.usecases.restaurante.item;

import com.postechfiap.meumenu.core.domain.entities.ItemCardapioDomain;
import java.util.UUID;

public interface BuscarItemCardapioPorIdUseCase {
    ItemCardapioDomain execute(UUID restauranteId, UUID itemId);
}