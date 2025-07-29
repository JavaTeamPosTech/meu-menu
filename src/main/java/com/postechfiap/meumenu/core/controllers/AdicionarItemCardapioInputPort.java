package com.postechfiap.meumenu.core.controllers;

import com.postechfiap.meumenu.core.dtos.restaurante.item.ItemCardapioInputModel;

import java.util.UUID;

public interface AdicionarItemCardapioInputPort {
    void execute(UUID restauranteId, ItemCardapioInputModel input);
}