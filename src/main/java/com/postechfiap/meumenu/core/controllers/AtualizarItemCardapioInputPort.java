package com.postechfiap.meumenu.core.controllers;

import com.postechfiap.meumenu.core.dtos.restaurante.item.ItemCardapioInputModel; // Importar

import java.util.UUID;

public interface AtualizarItemCardapioInputPort {
    void execute(UUID restauranteId, UUID itemId, ItemCardapioInputModel inputModel, UUID proprietarioLogadoId);
}