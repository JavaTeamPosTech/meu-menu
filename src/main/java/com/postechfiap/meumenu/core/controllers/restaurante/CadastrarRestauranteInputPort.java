package com.postechfiap.meumenu.core.controllers.restaurante;

import com.postechfiap.meumenu.core.dtos.restaurante.CadastrarRestauranteInputModel; // Importar

public interface CadastrarRestauranteInputPort {
    void execute(CadastrarRestauranteInputModel input);
}