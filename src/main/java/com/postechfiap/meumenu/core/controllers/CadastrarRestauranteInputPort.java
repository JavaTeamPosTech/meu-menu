package com.postechfiap.meumenu.core.controllers;

import com.postechfiap.meumenu.core.dtos.restaurante.CadastrarRestauranteInputModel; // Importar

public interface CadastrarRestauranteInputPort {
    void execute(CadastrarRestauranteInputModel input);
}