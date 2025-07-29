package com.postechfiap.meumenu.core.controllers;

import java.util.UUID;

public interface BuscarItemCardapioPorIdInputPort {
    void execute(UUID restauranteId, UUID itemId);
}