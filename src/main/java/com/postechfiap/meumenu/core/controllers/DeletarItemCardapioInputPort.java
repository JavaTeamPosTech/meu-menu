package com.postechfiap.meumenu.core.controllers;

import java.util.UUID;

public interface DeletarItemCardapioInputPort {
    void execute(UUID restauranteId, UUID itemId, UUID proprietarioLogadoId);
}