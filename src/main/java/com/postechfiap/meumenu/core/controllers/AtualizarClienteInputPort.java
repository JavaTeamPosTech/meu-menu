package com.postechfiap.meumenu.core.controllers;

import com.postechfiap.meumenu.core.dtos.cliente.AtualizarClienteInputModel;

public interface AtualizarClienteInputPort {
    void execute(AtualizarClienteInputModel input);
}