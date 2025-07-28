package com.postechfiap.meumenu.core.controllers;

import com.postechfiap.meumenu.core.dtos.cliente.CadastrarClienteInputModel;

public interface CadastrarClienteInputPort {
    void execute(CadastrarClienteInputModel input);
}
