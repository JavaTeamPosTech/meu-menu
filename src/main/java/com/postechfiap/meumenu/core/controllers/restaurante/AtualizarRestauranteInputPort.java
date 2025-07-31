package com.postechfiap.meumenu.core.controllers.restaurante;

import com.postechfiap.meumenu.core.dtos.restaurante.AtualizarRestauranteInputModel;
import java.util.UUID;

public interface AtualizarRestauranteInputPort {
    void execute(UUID restauranteId, AtualizarRestauranteInputModel inputModel);
}