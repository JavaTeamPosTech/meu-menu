package com.postechfiap.meumenu.core.domain.usecases.restaurante.item;

import com.postechfiap.meumenu.core.domain.entities.ItemCardapioDomain;
import com.postechfiap.meumenu.core.dtos.restaurante.item.ItemCardapioInputModel;

import java.util.UUID;

public interface AtualizarItemCardapioUseCase {
    ItemCardapioDomain execute(UUID restauranteId, UUID itemId, ItemCardapioInputModel itemCardapioAtualizado, UUID proprietarioLogadoId);
}