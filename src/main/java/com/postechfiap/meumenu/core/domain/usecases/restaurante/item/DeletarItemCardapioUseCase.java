package com.postechfiap.meumenu.core.domain.usecases.restaurante.item;

import java.util.UUID;

public interface DeletarItemCardapioUseCase {
    void execute(UUID restauranteId, UUID itemId, UUID proprietarioLogadoId);
}
