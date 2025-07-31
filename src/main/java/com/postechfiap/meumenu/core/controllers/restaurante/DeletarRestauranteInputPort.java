package com.postechfiap.meumenu.core.controllers.restaurante;

import java.util.UUID;

public interface DeletarRestauranteInputPort {
    void execute(UUID id, UUID proprietarioLogadoId);
}