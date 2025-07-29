package com.postechfiap.meumenu.core.domain.usecases.restaurante;

import java.util.UUID;

public interface DeletarRestauranteUseCase {
    void execute(UUID id, UUID proprietarioLogadoId);
}